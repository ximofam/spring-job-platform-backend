package com.htweb.core.services.impl;

import com.htweb.core.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeServiceImpl implements StripeService {
    @Override
    public PaymentIntent createPaymentIntent(Long amount, String currency, Map<String, Object> metadata) throws StripeException {
        PaymentIntentCreateParams.Builder builder = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency);

        if (metadata != null && !metadata.isEmpty()) {
            Map<String, String> stripeMetadata = new HashMap<>();

            metadata.forEach((key, value) -> {
                if (value != null) {
                    stripeMetadata.put(key, String.valueOf(value));
                }
            });

            builder.putAllMetadata(stripeMetadata);
        }

        return PaymentIntent.create(builder.build());
    }
}
