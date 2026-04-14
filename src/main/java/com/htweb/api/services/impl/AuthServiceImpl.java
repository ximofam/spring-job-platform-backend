package com.htweb.api.services.impl;

import com.htweb.api.dtos.TokenDto;
import com.htweb.api.exceptions.http.UnauthorizedException;
import com.htweb.api.services.AuthService;
import com.htweb.api.services.TokenService;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("apiAuthService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Qualifier("apiTokenService")
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserAuthRepository userAuthRepository;

    @Override
    @Transactional()
    public TokenDto.TokenResponse login(String username, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            TokenDto.AccessToken accessToken = tokenService.generateAccessToken(authentication);
            TokenDto.RefreshToken refreshToken = tokenService.generateRefreshToken(username);

            return new TokenDto.TokenResponse(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            System.out.println("LỖI ĐĂNG NHẬP SPRING SECURITY: " + e.getMessage());
            throw new UnauthorizedException("Incorrect username or password");
        }
    }

    @Override
    @Transactional
    public TokenDto.TokenResponse refreshToken(String rawToken) {
        RefreshToken refreshToken = tokenService.verifyAndGetRefreshToken(rawToken);
        tokenService.revokeRefreshToken(rawToken);

        User user = refreshToken.getUser();
        List<String> authorities = userAuthRepository.getAuthoritiesByUsername(user.getUsername());

        TokenDto.AccessToken accessToken = tokenService.generateAccessToken(
                user.getUsername(),
                Map.of("authorities", authorities));

        TokenDto.RefreshToken refreshTokenNew = tokenService.generateRefreshToken(refreshToken.getUser());

        return new TokenDto.TokenResponse(accessToken, refreshTokenNew);
    }
}
