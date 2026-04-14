package com.htweb.api.dtos;

public class AuthDto {
    private AuthDto() {
    }

    public record LoginRequest(
            String username,
            String password
    ) {
    }

    public record RefreshRequest(
            String token
    ) {
    }
}
