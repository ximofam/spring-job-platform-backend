package com.htweb.api.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank
        String usernameOrEmail,
        @NotBlank
        String password
) {
}