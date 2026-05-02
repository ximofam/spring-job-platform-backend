package com.htweb.api.services.impl;

import com.htweb.api.dtos.auth.AuthTokenResponse;
import com.htweb.api.dtos.token.AccessTokenResponse;
import com.htweb.api.dtos.token.RefreshTokenResponse;
import com.htweb.api.exceptions.tokens.TokenInvalidException;
import com.htweb.api.exceptions.users.IncorrectUsernameOrPasswordException;
import com.htweb.api.services.AuthService;
import com.htweb.api.services.TokenService;
import com.htweb.core.pojo.CustomUserDetails;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("apiAuthService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Qualifier("apiTokenService")
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthTokenResponse login(String usernameOrEmail, String password) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
            );
        } catch (AuthenticationException ex) {
            log.error("Auth login error: ", ex);
            throw new IncorrectUsernameOrPasswordException();
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        AccessTokenResponse accessToken = tokenService.generateAccessToken(user);
        RefreshTokenResponse refreshToken = tokenService.generateRefreshToken(user);

        return new AuthTokenResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthTokenResponse refreshToken(String rawToken) {
        RefreshToken refreshToken = tokenService.verifyAndGetRefreshToken(rawToken);
        tokenService.revokeRefreshToken(refreshToken.getId());

        User user = refreshToken.getUser();

        AccessTokenResponse accessToken = tokenService.generateAccessToken(user);
        RefreshTokenResponse refreshTokenNew = tokenService.generateRefreshToken(user);

        return new AuthTokenResponse(accessToken, refreshTokenNew);
    }

    @Override
    @Transactional
    public void logout(Long userId, String refreshTokenStr) {
        RefreshToken refreshToken = tokenService.verifyAndGetRefreshToken(refreshTokenStr);
        if (!refreshToken.getUser().getId().equals(userId)) {
            throw new TokenInvalidException();
        }

        tokenService.revokeRefreshToken(refreshToken.getId());
    }
}
