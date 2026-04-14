package com.htweb.api.services;

import com.htweb.api.dtos.TokenDto;

public interface AuthService {
    TokenDto.TokenResponse login(String username, String password);

    TokenDto.TokenResponse refreshToken(String rawToken);
}
