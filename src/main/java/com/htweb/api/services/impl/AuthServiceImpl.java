package com.htweb.api.services.impl;

import com.htweb.api.dtos.auth.AuthRegisterEmployerRequest;
import com.htweb.api.dtos.auth.AuthRegisterRequest;
import com.htweb.api.dtos.auth.AuthTokenResponse;
import com.htweb.api.dtos.token.AccessTokenResponse;
import com.htweb.api.dtos.user.UserSimpleResponse;
import com.htweb.api.exceptions.http.BadRequestException;
import com.htweb.api.exceptions.tokens.TokenInvalidException;
import com.htweb.api.exceptions.users.IncorrectUsernameOrPasswordException;
import com.htweb.api.mappers.CompanyMapper;
import com.htweb.api.mappers.UserMapper;
import com.htweb.api.repositories.CountryRepository;
import com.htweb.api.repositories.RoleRepository;
import com.htweb.api.repositories.UserRepository;
import com.htweb.api.services.AuthService;
import com.htweb.api.services.CompanyService;
import com.htweb.api.services.TokenService;
import com.htweb.core.enums.CompanyStatus;
import com.htweb.core.enums.UserRole;
import com.htweb.core.helpers.security.CustomUserDetails;
import com.htweb.core.pojo.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("apiAuthService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Qualifier("apiTokenService")
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    @Qualifier("apiUserRepository")
    private final UserRepository userRepository;
    @Qualifier("apiCountryRepository")
    private final CountryRepository countryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    @Qualifier("apiRoleRepository")
    private final RoleRepository roleRepository;
    @Qualifier("apiCompanyService")
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional
    public AuthTokenResponse login(String usernameOrEmail, String password) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
            );
        } catch (AuthenticationException ex) {
            log.error("Auth login error: ", ex);
            throw new IncorrectUsernameOrPasswordException();
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        return generateTokens(user);
    }

    @Override
    @Transactional
    public AuthTokenResponse refreshToken(String rawToken) {
        RefreshToken refreshToken = tokenService.verifyAndGetRefreshToken(rawToken);
        tokenService.revokeRefreshToken(refreshToken.getId());

        User user = refreshToken.getUser();

        return generateTokens(user);
    }

    @Override
    @Transactional
    public void logout(Long userId, String refreshTokenStr) {
        RefreshToken refreshToken = tokenService.verifyAndGetRefreshToken(refreshTokenStr);
        if (!refreshToken.getUser().getId().equals(userId)) {
            throw new TokenInvalidException();
        }

        tokenService.revokeRefreshToken(refreshToken.getId());
    }

    @Override
    public boolean isFieldExists(String field, String value) {
        return switch (field) {
            case "email" -> userRepository.isExistsEmail(value);
            case "username" -> userRepository.isExistsUsername(value);
            case "companyTaxCode" -> companyService.isExistsTaxCode(value);
            default -> throw new BadRequestException("Invalid field: %s", field);
        };
    }

    @Override
    @Transactional
    public UserSimpleResponse registerCandidate(AuthRegisterRequest request) {
        String hashPassword = passwordEncoder.encode(request.getPassword());

        User user = userMapper.toUser(request);
        user.setPasswordHash(hashPassword);
        user.setUserRole(UserRole.CANDIDATE);
        roleRepository.findByName("CANDIDATE").ifPresent(role -> user.getRoles().add(role));

        try {
            userRepository.save(user);
        } catch (ConstraintViolationException ex) {
            handleDuplicateEntry(ex);
        }

        CandidateProfile profile = new CandidateProfile();
        profile.setUser(user);
        userRepository.createCandidateProfile(profile);

        return userMapper.toUserSimpleResponse(user);
    }

    @Override
    @Transactional
    public UserSimpleResponse registerEmployer(AuthRegisterEmployerRequest request) {
        String hashPassword = passwordEncoder.encode(request.getPassword());

        User user = userMapper.toUser(request);
        user.setPasswordHash(hashPassword);
        user.setUserRole(UserRole.EMPLOYER);

        try {
            userRepository.save(user);
        } catch (ConstraintViolationException ex) {
            handleDuplicateEntry(ex);
        }

        Company company = companyMapper.toCompany(request);
        company.setStatus(CompanyStatus.PENDING);
        companyService.create(company);

        EmployerProfile profile = new EmployerProfile();
        profile.setUser(user);
        profile.setCompany(company);
        try {
            userRepository.createEmployerProfile(profile);
        } catch (ConstraintViolationException ex) {
            handleDuplicateEntry(ex);
        }

        return userMapper.toUserSimpleResponse(user);
    }


    private AuthTokenResponse generateTokens(User user) {
        AccessTokenResponse accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        AuthTokenResponse tokenRes = new AuthTokenResponse();
        tokenRes.setRefreshToken(refreshToken);
        tokenRes.setAccessToken(accessToken.getToken());
        tokenRes.setAccessTokenExpiresIn(accessToken.getExpiresIn());

        return tokenRes;
    }

    private void handleDuplicateEntry(ConstraintViolationException ex) {
        String constraintName = ex.getConstraintName();
        if (constraintName == null) {
            throw new BadRequestException("Duplicate entry");
        }

        throw switch (constraintName) {
            case "uq_users_username_active" -> new BadRequestException("Username already exists");
            case "uq_users_email_active" -> new BadRequestException("Email already exists");
            case "uk_companies_tax_code" -> new BadRequestException("Company tax code already exists");
            default -> new BadRequestException("Duplicate entry");
        };
    }
}
