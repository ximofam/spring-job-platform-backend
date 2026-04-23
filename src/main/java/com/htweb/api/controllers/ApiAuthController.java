package com.htweb.api.controllers;

import com.htweb.api.dtos.ApiResponse;
import com.htweb.api.dtos.auth.AuthLoginRequest;
import com.htweb.api.dtos.auth.AuthLogoutRequest;
import com.htweb.api.dtos.auth.AuthRefreshRequest;
import com.htweb.api.dtos.auth.AuthTokenResponse;
import com.htweb.api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid AuthLoginRequest request) {
        String usernameOrEmail = request.usernameOrEmail();
        String password = request.password();

        return ResponseEntity.ok(authService.login(usernameOrEmail, password));
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthTokenResponse> refresh(@RequestBody @Valid AuthRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.token()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AuthLogoutRequest request) {

        authService.logout(userId, request.refreshToken());

        return ResponseEntity.ok(
                new ApiResponse("Logout successfully", null)
        );
    }
}
