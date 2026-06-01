package com.htweb.core.services.impl;

import com.htweb.core.services.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:config.properties")
public class StripeServiceImpl implements StripeService {
    @Value("${payment.stripe.webhook-secret}")
    private String webhookSecret;

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

    @Override
    public Event verifyWebhook(String payload, String sigHeader) throws SignatureVerificationException {
        return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    }
}
