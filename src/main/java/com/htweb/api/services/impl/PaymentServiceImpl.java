package com.htweb.api.services.impl;

import com.htweb.api.dtos.payment.PaymentProcessRequest;
import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.exceptions.http.UnauthorizedException;
import com.htweb.api.repositories.JobRepository;
import com.htweb.api.repositories.PaymentRepository;
import com.htweb.api.repositories.UserRepository;
import com.htweb.api.services.PaymentService;
import com.htweb.core.enums.JobStatus;
import com.htweb.core.enums.PaymentStatus;
import com.htweb.core.pojo.Job;
import com.htweb.core.pojo.Payment;
import com.htweb.core.publishers.JobPostPublisher;
import com.htweb.core.publishers.NotificationPublisher;
import com.htweb.core.services.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service("apiPaymentService")
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Qualifier("apiPaymentRepository")
    private final PaymentRepository paymentRepository;
    @Qualifier("apiUserRepository")
    private final UserRepository userRepository;
    private final StripeService stripeService;
    @Qualifier("apiJobRepository")
    private final JobRepository jobRepository;
    private final NotificationPublisher notificationPublisher;
    private final JobPostPublisher jobPostPublisher;

    @Override
    @Transactional
    public Map<String, Object> stripeProcess(Long userId, PaymentProcessRequest request) {
        Payment payment = handlerPaymentService(userId, request);

        try {
            Map<String, Object> stripeMetadata = payment.getMetadata();
            stripeMetadata.put("payment_id", payment.getId());

            PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                    payment.getAmount().longValue(),
                    payment.getCurrency(),
                    stripeMetadata
            );


            payment.setTransactionId(paymentIntent.getId());
            paymentRepository.save(payment);

            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("paymentId", payment.getId());

            return response;

        } catch (StripeException e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            throw new RuntimeException("Lỗi tạo thanh toán Stripe: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void stripeWebhook(String payload, String sigHeader) {
        Event event;

        try {
            event = stripeService.verifyWebhook(payload, sigHeader);
        } catch (SignatureVerificationException e) {
            throw new UnauthorizedException("Chữ ký Stripe Webhook không hợp lệ", e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi parse payload Webhook", e);
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            System.out.println("API version mismatch. Đang sử dụng deserializeUnsafe().");
            try {
                stripeObject = dataObjectDeserializer.deserializeUnsafe();
            } catch (Exception e) {
                throw new RuntimeException("Không thể deserialize dữ liệu (cả an toàn lẫn không an toàn)", e);
            }
        }

        if (stripeObject == null) {
            throw new RuntimeException("Hoàn toàn không thể đọc dữ liệu từ Stripe Event");
        }

        switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                Map<String, String> metadata = paymentIntent.getMetadata();

                if (metadata != null && metadata.containsKey("payment_id")) {
                    String paymentIdStr = metadata.get("payment_id");
                    Long paymentId = Long.parseLong(paymentIdStr);

                    handleSuccess(paymentId);
                } else {
                    System.err.println("Webhook nhận được nhưng không có 'payment_id' trong metadata.");
                }
            }

            case "payment_intent.payment_failed" -> {
                PaymentIntent failedIntent = (PaymentIntent) stripeObject;

            }

            default -> System.out.println("Bỏ qua sự kiện không cần thiết: " + event.getType());
        }
    }

    @Override
    public Payment handlerPaymentService(Long userId, PaymentProcessRequest request) {
        Map<String, Object> metadata = request.getMetadata();
        if (!metadata.containsKey("service")) {
            return null;
        }

        String service = String.valueOf(metadata.get("service"));

        if ("JOB_FEATURED".equals(service)) {
            Payment payment = new Payment();
            payment.setUser(userRepository.getReference(userId));
            payment.setAmount(BigDecimal.valueOf(99000));
            payment.setCurrency("vnd");
            payment.setMethod(request.getMethod());
            payment.setMetadata(new HashMap<>(metadata));
            paymentRepository.save(payment);
            return payment;
        }

        throw new BadRequestException("Dịch vụ không được hỗ trợ: " + service);
    }

    protected void handleSuccess(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy payment với id: %d", paymentId));

        Map<String, Object> metadata = payment.getMetadata();
        if (!metadata.containsKey("service")) {
            return;
        }

        String service = String.valueOf(metadata.get("service"));
        if ("JOB_FEATURED".equals(service)) {
            Long jobId = Long.parseLong(metadata.get("jobId").toString());
            Job job = jobRepository.findById(jobId).orElse(null);
            if (job == null) return;

            Instant now = Instant.now();
            job.setStatus(JobStatus.PUBLISHED);
            job.setPublishedAt(now);
            job.setExpiredAt(now.plus(30, ChronoUnit.DAYS));
            job.setBoostScore(10);
            jobRepository.update(job);

            notificationPublisher.publish(
                    payment.getUserId(),
                    "Thanh toán thành công",
                    String.format("Tin nổi bật\"%s\" Đã được đăng", job.getTitle()),
                    Map.of("jobId", job.getId()));
            
            jobPostPublisher.publish(job.getId());
        }

        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.update(payment);
    }
}
