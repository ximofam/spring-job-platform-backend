package com.htweb.core.services.impl;

import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.exceptions.http.InternalServerException;
import com.htweb.api.exceptions.http.UnauthorizedException;
import com.htweb.core.helpers.dtos.AccessTokenResponse;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import com.htweb.core.services.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = false)
public class JwtServiceImpl implements JwtService {

    @Value("${token.jwt.secret-key}")
    private String secretKey;

    @Value("${token.access-token.ttl.seconds}")
    private long accessTokenTtlSeconds;

    private long accessTokenTtlMillis;
    private JWSSigner jwtSigner;
    private JWSVerifier jwtVerifier;

    @PostConstruct
    public void init() {
        try {
            this.accessTokenTtlMillis = this.accessTokenTtlSeconds * 1000;
            this.jwtSigner = new MACSigner(secretKey);
            this.jwtVerifier = new MACVerifier(secretKey);
        } catch (KeyLengthException e) {
            throw new InternalServerException("Invalid secret key: " + e.getMessage());
        } catch (JOSEException e) {
            throw new InternalServerException("Token verification init failed: " + e.getMessage());
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
    public AccessTokenResponse generateAccessToken(User user) {
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

        return new AccessTokenResponse(signedJWT.serialize(), exp.getTime());
    }
}