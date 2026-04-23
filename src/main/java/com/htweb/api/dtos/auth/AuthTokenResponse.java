package com.htweb.api.dtos.auth;

import com.htweb.api.dtos.token.AccessTokenResponse;
import com.htweb.api.dtos.token.RefreshTokenResponse;

public record AuthTokenResponse(
        AccessTokenResponse accessToken,
        RefreshTokenResponse refreshToken
) {
}
