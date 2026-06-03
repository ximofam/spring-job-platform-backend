package com.htweb.api.controllers;

import com.htweb.api.dtos.ApiResponse;
import com.htweb.api.dtos.application.CandidateCvResponse;
import com.htweb.api.dtos.job.MyJobResponse;
import com.htweb.api.dtos.user.*;
import com.htweb.api.services.JobService;
import com.htweb.api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ApiUserController {
    @Qualifier("apiUserService")
    private final UserService userService;
    @Qualifier("apiJobService")
    private final JobService jobService;

    @GetMapping("/me")
    public ResponseEntity<UserDetailResponse> getMe(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getUserDetailById(userId));
    }


    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('user:view') or hasRole('ADMIN')")
    public ResponseEntity<UserDetailResponse> getUserDetailByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserDetailByUsername(username));
    }

    @PatchMapping("/me")
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
            @RequestBody @Valid EducationCreateRequest request) {

        userService.addEducationForCandidate(userId, request);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/me/experiences")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> addExperienceForCandidate(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ExperienceCreateRequest request) {

        userService.addExperienceForCandidate(userId, request);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<ApiResponse> uploadAvatar(
            @AuthenticationPrincipal Long userId,
            @ModelAttribute @Valid UserAvatarUploadRequest request) {

        String avatarUrl = userService.uploadAvatar(userId, request.getFile());
        ApiResponse apiResponse = new ApiResponse(
                "Đã upload thành công avatar cho user",
                Map.of("avatarUrl", avatarUrl));

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/upload-cv")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse> uploadCV(
            @AuthenticationPrincipal Long userId,
            @ModelAttribute @Valid UserCVUploadRequest request) {

        String cvUrl = userService.uploadCV(userId, request);
        ApiResponse apiResponse = new ApiResponse(
                "Đã upload thành công cv cho user",
                Map.of("cvUrl", cvUrl));

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/my-cvs")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<CandidateCvResponse>> getMyCVs(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getCandidateCVs(userId));
    }

    @GetMapping("/me/jobs")
    @PreAuthorize("hasAuthority('job:create')")
    public ResponseEntity<List<MyJobResponse>> getMyJobs(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(jobService.getMyJobs(userId));
    }
    
}
