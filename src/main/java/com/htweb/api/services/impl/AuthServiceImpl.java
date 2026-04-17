package com.htweb.api.services.impl;

import com.htweb.api.dtos.TokenDto;
import com.htweb.api.dtos.UserDto;
import com.htweb.api.exceptions.tokens.TokenInvalidException;
import com.htweb.api.exceptions.users.IncorrectUsernameOrPasswordException;
import com.htweb.api.exceptions.users.UserNotFoundException;
import com.htweb.api.mappers.UserMapper;
import com.htweb.api.services.AuthService;
import com.htweb.api.services.TokenService;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("apiAuthService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Qualifier("apiTokenService")
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthRepository userAuthRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public TokenDto.TokenResponse login(String username, String password) {
        User user = authenticate(username, password);

        TokenDto.AccessToken accessToken = tokenService.generateAccessToken(user);
        TokenDto.RefreshToken refreshToken = tokenService.generateRefreshToken(user);

        return new TokenDto.TokenResponse(accessToken, refreshToken);

    }

    @Override
    @Transactional
    public TokenDto.TokenResponse refreshToken(String rawToken) {
        RefreshToken refreshToken = tokenService.verifyAndGetRefreshToken(rawToken);
        tokenService.revokeRefreshToken(rawToken);

        User user = refreshToken.getUser();

        TokenDto.AccessToken accessToken = tokenService.generateAccessToken(user);
        TokenDto.RefreshToken refreshTokenNew = tokenService.generateRefreshToken(user);

        return new TokenDto.TokenResponse(accessToken, refreshTokenNew);
    }

    @Override
    @Transactional
    public void logout(String username, String refreshTokenStr) {
        RefreshToken refreshToken = tokenService.verifyAndGetRefreshToken(refreshTokenStr);
        if (!refreshToken.getUser().getUsername().equals(username)) {
            throw new TokenInvalidException();
        }

        tokenService.revokeRefreshToken(refreshTokenStr);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto.DetailResponse getUserByUsername(String username) {
        User user = userAuthRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return userMapper.toDetailResponse(user);
    }

    private User authenticate(String username, String password) {
        User user = userAuthRepository.findUserByUsername(username)
                .orElseThrow(IncorrectUsernameOrPasswordException::new);

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IncorrectUsernameOrPasswordException();
        }

        return user;
    }
}
