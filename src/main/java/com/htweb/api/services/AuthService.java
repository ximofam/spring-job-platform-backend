package com.htweb.api.services;

import com.htweb.api.dtos.TokenDto;

public interface AuthService {
    TokenDto.TokenResponse login(String usernameOrEmail, String password);

    TokenDto.TokenResponse refreshToken(String rawToken);

    void logout(String username, String refreshToken);
}
