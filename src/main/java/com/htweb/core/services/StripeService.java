package com.htweb.core.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.Map;

public interface StripeService {
    PaymentIntent createPaymentIntent(Long amount, String currency, Map<String, Object> metadata) throws StripeException;
}
