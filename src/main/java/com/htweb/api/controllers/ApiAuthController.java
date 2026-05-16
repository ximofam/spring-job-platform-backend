package com.htweb.api.controllers;

import com.htweb.api.dtos.ApiResponse;
import com.htweb.api.dtos.auth.*;
import com.htweb.api.dtos.user.UserSimpleResponse;
import com.htweb.api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.ok(new ApiResponse("Logout successfully", null));
    }

    @GetMapping("/check-unique")
    public ResponseEntity<AuthCheckFieldResponse> checkUniqueField(
            @RequestParam String field,
            @RequestParam String value) {

        boolean exist = authService.isFieldExists(field, value);

        return ResponseEntity.ok(new AuthCheckFieldResponse(field, !exist));
    }


    @PostMapping("/register/candidate")
    public ResponseEntity<ApiResponse> registerCandidate(@RequestBody @Valid AuthRegisterRequest request) {
        UserSimpleResponse userRes = authService.registerCandidate(request);

        return ResponseEntity.status(201).body(
                new ApiResponse("Register candidate successfully!", userRes)
        );
    }

    @PostMapping(value = "/register/employer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> registerEmployer(@ModelAttribute @Valid AuthRegisterEmployerRequest request) {
        UserSimpleResponse userRes = authService.registerEmployer(request);

        return ResponseEntity.status(201).body(
                new ApiResponse("Register employer successfully!", userRes)
        );
    }
}
