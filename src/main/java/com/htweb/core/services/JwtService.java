package com.htweb.core.services;


import com.htweb.core.helpers.dtos.AccessTokenResponse;
import com.htweb.core.pojo.User;
import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtService {
    AccessTokenResponse generateAccessToken(User user);

    JWTClaimsSet verifyAndParseAccessToken(String token);
}
