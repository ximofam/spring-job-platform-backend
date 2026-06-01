package com.htweb.api.dtos.payment;

import com.htweb.core.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PaymentProcessRequest {
    @NotNull
    private PaymentMethod method;
    @NotNull
    private Map<String, Object> metadata;
}
