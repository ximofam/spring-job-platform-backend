package com.htweb.api.services;

import com.htweb.api.dtos.TokenDto;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface TokenService {
    TokenDto.AccessToken generateAccessToken(String subject, Map<String, Object> info);

    TokenDto.AccessToken generateAccessToken(Authentication authentication);

    JWTClaimsSet verifyAndParseAccessToken(String token);

    TokenDto.RefreshToken generateRefreshToken(String username);

    TokenDto.RefreshToken generateRefreshToken(User user);

    RefreshToken verifyAndGetRefreshToken(String rawToken);

    void revokeRefreshToken(String rawToken);
}
