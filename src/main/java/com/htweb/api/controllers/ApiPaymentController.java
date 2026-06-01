package com.htweb.api.controllers;

import com.htweb.api.dtos.payment.PaymentProcessRequest;
import com.htweb.api.services.PaymentService;
import com.htweb.core.enums.PaymentMethod;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class ApiPaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> payment(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid PaymentProcessRequest request) {

        Map<String, Object> res = null;
        if (request.getMethod().equals(PaymentMethod.STRIPE)) {
            res = paymentService.stripeProcess(userId, request);
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/webhook/stripe")
    public ResponseEntity<?> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        paymentService.stripeWebhook(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}
