package com.htweb.api.services;

import com.htweb.api.dtos.auth.AuthRegisterEmployerRequest;
import com.htweb.api.dtos.auth.AuthRegisterRequest;
import com.htweb.api.dtos.auth.AuthTokenResponse;
import com.htweb.api.dtos.user.UserSimpleResponse;

public interface AuthService {
    AuthTokenResponse login(String usernameOrEmail, String password);

    AuthTokenResponse refreshToken(String rawToken);

    void logout(Long userId, String refreshToken);

    boolean isFieldExists(String field, String value);

    UserSimpleResponse registerCandidate(AuthRegisterRequest request);

    UserSimpleResponse registerEmployer(AuthRegisterEmployerRequest request);
}
