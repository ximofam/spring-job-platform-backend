package com.htweb.api.services;

import com.htweb.api.dtos.auth.AuthTokenResponse;

public interface AuthService {
    AuthTokenResponse login(String usernameOrEmail, String password);

    AuthTokenResponse refreshToken(String rawToken);

    void logout(Long userId, String refreshToken);
}
