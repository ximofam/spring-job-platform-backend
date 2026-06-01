package com.htweb.api.services;

import com.htweb.api.dtos.payment.PaymentProcessRequest;

import java.util.Map;

public interface PaymentService {
    Map<String, Object> process(PaymentProcessRequest request);


}
