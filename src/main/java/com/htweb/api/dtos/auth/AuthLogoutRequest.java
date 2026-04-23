package com.htweb.api.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLogoutRequest(
        @NotBlank
        String refreshToken
) {
}