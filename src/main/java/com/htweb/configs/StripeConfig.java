package com.htweb.configs;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class StripeConfig {

    @Value("${payment.stripe.secret-key}")
    private String stripeApiKey;

    @PostConstruct
    public void setupStripe() {
        Stripe.apiKey = stripeApiKey;
    }
}
