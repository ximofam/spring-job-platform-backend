package com.htweb.api.controllers;

import com.htweb.api.dtos.AuthDto;
import com.htweb.api.dtos.TokenDto;
import com.htweb.api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {
    @Qualifier("apiAuthService")
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<TokenDto.TokenResponse> login(@RequestBody AuthDto.LoginRequest request) {
        String username = request.username();
        String password = request.password();

        return ResponseEntity.ok(authService.login(username, password));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto.TokenResponse> refresh(@RequestBody AuthDto.RefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.token()));
    }
}
