package com.htweb.api.controllers;

import com.htweb.api.dtos.ApiResponse;
import com.htweb.api.dtos.AuthDto;
import com.htweb.api.dtos.TokenDto;
import com.htweb.api.dtos.UserDto;
import com.htweb.api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiAuthController {
    @Qualifier("apiAuthService")
    private final AuthService authService;


    @PostMapping("auth/login")
    public ResponseEntity<TokenDto.TokenResponse> login(@RequestBody @Valid AuthDto.LoginRequest request) {
        String username = request.username();
        String password = request.password();

        return ResponseEntity.ok(authService.login(username, password));
    }

    @PostMapping("auth/refresh")
    public ResponseEntity<TokenDto.TokenResponse> refresh(@RequestBody @Valid AuthDto.RefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.token()));
    }

    @GetMapping("/secure/auth/me")
    public ResponseEntity<UserDto.DetailResponse> getMe(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(authService.getUserByUsername(username));
    }

    @PostMapping("/secure/auth/logout")
    public ResponseEntity<ApiResponse> logout(
            @AuthenticationPrincipal String username,
            @RequestBody @Valid AuthDto.LogoutRequest request) {

        authService.logout(username, request.refreshToken());
        
        return ResponseEntity.ok(
                new ApiResponse("Logout successfully", null)
        );
    }
}
