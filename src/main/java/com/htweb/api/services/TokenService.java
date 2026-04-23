package com.htweb.api.services;

import com.htweb.api.dtos.token.AccessTokenResponse;
import com.htweb.api.dtos.token.RefreshTokenResponse;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;
import com.nimbusds.jwt.JWTClaimsSet;

public interface TokenService {
    AccessTokenResponse generateAccessToken(User user);

    JWTClaimsSet verifyAndParseAccessToken(String token);

    RefreshTokenResponse generateRefreshToken(User user);

    RefreshToken verifyAndGetRefreshToken(String rawToken);

    void revokeRefreshToken(Long tokenId);
}
