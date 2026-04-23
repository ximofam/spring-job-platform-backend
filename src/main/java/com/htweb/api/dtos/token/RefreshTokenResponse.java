package com.htweb.api.dtos.token;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record RefreshTokenResponse(
        String token,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime expiresAt
) {
}
