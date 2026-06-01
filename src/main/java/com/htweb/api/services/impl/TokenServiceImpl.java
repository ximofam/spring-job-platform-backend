package com.htweb.api.services.impl;


import com.htweb.api.exceptions.http.InternalServerException;
import com.htweb.api.exceptions.http.UnauthorizedException;
import com.htweb.api.repositories.RefreshTokenRepository;
import com.htweb.api.services.TokenService;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HexFormat;

@Service("apiTokenService")
@RequiredArgsConstructor
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = false)
public class TokenServiceImpl implements TokenService {
    @Qualifier("apiRefreshTokenRepository")
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${token.refresh-token.ttl.days}")
    private long refreshTokenTtlDays;
    private long refreshTokenTtlSeconds;

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    @PostConstruct
    public void init() {
        this.refreshTokenTtlSeconds = this.refreshTokenTtlDays * 24 * 60 * 60;
    }


    @Override
    public String generateRefreshToken(User user) {
        String rawToken = generateRefreshTokenStr();
        String tokenHash = hashRefreshTokenStr(rawToken);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(refreshTokenTtlSeconds));

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Override
    public RefreshToken verifyAndGetRefreshToken(String rawToken) {
        String tokenHash = hashRefreshTokenStr(rawToken);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        if (refreshToken.isRevoked()) {
            throw new UnauthorizedException("Token has been revoked");
        }

        if (refreshToken.isExpired()) {
            throw new UnauthorizedException("Token has expired");
        }

        return refreshToken;
    }

    @Override
    public void revokeRefreshToken(Long tokenId) {
        if (!refreshTokenRepository.revokeTokenById(tokenId)) {
            throw new UnauthorizedException("Invalid token");
        }
    }


    private String generateRefreshTokenStr() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String hashRefreshTokenStr(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}