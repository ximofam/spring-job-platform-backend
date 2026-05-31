package com.htweb.api.controllers;

import com.htweb.api.dtos.application.ApplicationCreateRequest;
import com.htweb.api.dtos.application.ApplicationDetailResponse;
import com.htweb.api.dtos.application.ApplicationReviewRequest;
import com.htweb.api.dtos.application.ApplicationSimpleResponse;
import com.htweb.api.services.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApiApplicationController {
    @Qualifier("apiApplicationService")
    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasAuthority('application:apply')")
    public ResponseEntity<ApplicationSimpleResponse> createApplication(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ApplicationCreateRequest request) {

        ApplicationSimpleResponse res = applicationService.createApplication(userId, request);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('application:view')")
    public ResponseEntity<List<ApplicationSimpleResponse>> getApplications(@AuthenticationPrincipal Long userId) {

        return ResponseEntity.ok(applicationService.getApplications(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('application:view')")
    public ResponseEntity<ApplicationDetailResponse> getDetailApplications(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {

        return ResponseEntity.ok(applicationService.getDetailApplication(userId, id));
    }

    @PostMapping("/{id}/review")
    @PreAuthorize("hasAuthority('application:review')")
    public ResponseEntity<Void> reviewApplication(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestBody @Valid ApplicationReviewRequest request) {

        applicationService.reviewApplication(userId, id, request);

        return ResponseEntity.ok().build();
    }
}
