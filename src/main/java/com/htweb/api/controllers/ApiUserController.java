package com.htweb.api.controllers;

import com.htweb.api.dtos.user.EducationCreateRequest;
import com.htweb.api.dtos.user.ExperienceCreateRequest;
import com.htweb.api.dtos.user.UserDetailResponse;
import com.htweb.api.dtos.user.UserUpdateRequest;
import com.htweb.api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ApiUserController {
    @Qualifier("apiUserService")
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDetailResponse> getMe(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getUserDetailById(userId));
    }


    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('user:view') or hasRole('ADMIN')")
    public ResponseEntity<UserDetailResponse> getUserDetailByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserDetailByUsername(username));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal Long userId,
            @ModelAttribute UserUpdateRequest request) {

        userService.updateUser(userId, request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/educations")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> addEducationForCandidate(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody EducationCreateRequest request) {

        userService.addEducationForCandidate(userId, request);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/me/experiences")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> addExperienceForCandidate(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ExperienceCreateRequest request) {

        userService.addExperienceForCandidate(userId, request);

        return ResponseEntity.status(201).build();
    }
}
