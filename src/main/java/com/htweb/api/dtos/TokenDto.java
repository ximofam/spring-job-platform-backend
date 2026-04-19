package com.htweb.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public final class TokenDto {
    private TokenDto() {
    }


    public record AccessToken(
            String token,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime expiresAt
    ) {
    }


    public record RefreshToken(
            String token,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime expiresAt
    ) {
    }

    public record TokenResponse(AccessToken accessToken, RefreshToken refreshToken) {
    }
}
