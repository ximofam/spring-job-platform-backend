package com.htweb.api.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.htweb.api.dtos.TokenDto;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;
import com.nimbusds.jwt.JWTClaimsSet;

public interface TokenService {
    TokenDto.AccessToken generateAccessToken(User user);

    JWTClaimsSet verifyAndParseAccessToken(String token);

    TokenDto.RefreshToken generateRefreshToken(User user);

    RefreshToken verifyAndGetRefreshToken(String rawToken);

    void revokeRefreshToken(Long tokenId);

    GoogleIdToken.Payload verifyGoogleToken(String token);
}
