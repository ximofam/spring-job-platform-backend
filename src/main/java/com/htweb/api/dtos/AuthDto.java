package com.htweb.api.dtos;

import jakarta.validation.constraints.NotBlank;

public class AuthDto {
    private AuthDto() {
    }

    public record LoginRequest(
            @NotBlank
            String username,
            @NotBlank
            String password
    ) {
    }

    public record RefreshRequest(
            @NotBlank
            String token
    ) {
    }

    public record LogoutRequest(
            @NotBlank
            String refreshToken
    ) {
    }
}
