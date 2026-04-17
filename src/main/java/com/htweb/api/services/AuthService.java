package com.htweb.api.services;

import com.htweb.api.dtos.TokenDto;
import com.htweb.api.dtos.UserDto;

public interface AuthService {
    TokenDto.TokenResponse login(String username, String password);

    TokenDto.TokenResponse refreshToken(String rawToken);

    void logout(String username, String refreshToken);

    UserDto.DetailResponse getUserByUsername(String username);
}
