package com.htweb.api.services.impl;

import com.htweb.api.dtos.payment.PaymentProcessRequest;
import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.repositories.PaymentRepository;
import com.htweb.api.services.PaymentService;
import com.htweb.core.enums.PaymentMethod;
import com.htweb.core.pojo.Payment;
import com.htweb.core.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("apiPaymentService")
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Qualifier("apiPaymentRepository")
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    @Override
    public Map<String, Object> process(PaymentProcessRequest request) {
        Payment payment = new Payment();

        Map<String, Object> response = new HashMap<>();
        switch (request.getMethod()) {
            case PaymentMethod.STRIPE -> {
                try {
                    PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                            payment.getAmount().longValue(),
                            payment.getCurrency(),
                            payment.getMetadata());


                    response.put("clientSecret", paymentIntent.getClientSecret());
                    response.put("paymentId", payment.getId());

                } catch (StripeException e) {
                    throw new RuntimeException(e);
                }
            }
            case PaymentMethod.MOMO ->
                    throw new BadRequestException("Phương thức thanh toán không được hỗ trợ: " + request.getMethod());
            default ->
                    throw new BadRequestException("Phương thức thanh toán không được hỗ trợ: " + request.getMethod());
        }
        ;

        return response;
    }

    public static Payment handlerPaymentService(Map<String, Object> metadata) {

        return new Payment();
    }
}
