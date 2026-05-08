package com.htweb.api.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthTokenResponse {
    private String refreshToken;
    private String accessToken;
    private long accessTokenExpiresIn;
    private String accessTokenType = "Bearer";
}
