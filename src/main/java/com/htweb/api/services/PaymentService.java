package com.htweb.api.services;

import com.htweb.api.dtos.payment.PaymentProcessRequest;
import com.htweb.core.pojo.Payment;

import java.util.Map;

public interface PaymentService {
    Map<String, Object> stripeProcess(Long userId, PaymentProcessRequest request);

    void stripeWebhook(String payload, String sigHeader);

    Payment handlerPaymentService(Long userId, PaymentProcessRequest request);
}
