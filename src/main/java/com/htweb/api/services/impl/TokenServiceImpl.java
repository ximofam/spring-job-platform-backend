package com.htweb.api.services.impl;


import com.htweb.api.dtos.TokenDto;
import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.exceptions.http.InternalServerException;
import com.htweb.api.exceptions.http.UnauthorizedException;
import com.htweb.api.repositories.RefreshTokenRepository;
import com.htweb.api.services.TokenService;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HexFormat;
import java.util.List;

@Service("apiTokenService")
@RequiredArgsConstructor
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = false)
public class TokenServiceImpl implements TokenService {
    @Qualifier("apiRefreshTokenRepository")
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAuthRepository userAuthRepository;

    @Value("${token.jwt.secret-key}")
    private String secretKey;
    @Value("${token.access-token.ttl.millis}")
    private long accessTokenTtlMillis;
    @Value("${token.refresh-token.ttl.days}")
    private long refreshTokenTtlDays;

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    private JWSSigner jwtSigner;
    private JWSVerifier jwtVerifier;

    @PostConstruct
    public void init() {
        try {
            this.jwtSigner = new MACSigner(secretKey);
            this.jwtVerifier = new MACVerifier(secretKey);
        } catch (KeyLengthException e) {
            throw new InternalServerException("Invalid secret key: %s", e.getMessage());
        } catch (JOSEException e) {
            throw new InternalServerException("Token verification failed: %s", e.getMessage());
        }
    }

    @Override
    public JWTClaimsSet verifyAndParseAccessToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(jwtVerifier)) {
                throw new UnauthorizedException("Invalid token signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            if (claims.getExpirationTime().before(new Date())) {
                throw new UnauthorizedException("Token has expired");
            }

            return claims;

        } catch (ParseException e) {
            throw new BadRequestException("Malformed token");
        } catch (JOSEException e) {
            throw new InternalServerException("Token verification failed");
        }
    }

    @Override
    public TokenDto.AccessToken generateAccessToken(User user) {
        Date exp = new Date(System.currentTimeMillis() + accessTokenTtlMillis);

        List<String> roles = user.getRoles().stream().map(Role::getName).toList();

        JWTClaimsSet.Builder claimsSetBd = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .claim("roles", roles)
                .issueTime(new Date())
                .expirationTime(exp);


        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSetBd.build()
        );

        try {
            signedJWT.sign(jwtSigner);
        } catch (JOSEException e) {
            throw new InternalServerException(e.getMessage());
        }

        LocalDateTime ldt = exp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return new TokenDto.AccessToken(signedJWT.serialize(), ldt);
    }

    @Override
    public TokenDto.RefreshToken generateRefreshToken(User user) {
        String rawToken = generateRefreshTokenStr();
        String tokenHash = hashRefreshTokenStr(rawToken);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(refreshTokenTtlDays * 24 * 60 * 60));

        RefreshToken refreshTokenCreated = refreshTokenRepository.save(refreshToken);

        LocalDateTime ldt = LocalDateTime.ofInstant(
                refreshTokenCreated.getExpiresAt(), ZoneId.systemDefault()
        );
        
        return new TokenDto.RefreshToken(rawToken, ldt);
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
