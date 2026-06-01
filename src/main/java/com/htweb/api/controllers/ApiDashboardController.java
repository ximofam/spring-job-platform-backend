package com.htweb.api.controllers;

import com.htweb.api.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class ApiDashboardController {

    @Qualifier("apiDashboardService")
    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public ResponseEntity<?> getProcessingSpeed(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "2024") Integer year
    ) {
        return ResponseEntity.ok(dashboardService.getProcessingSpeedReviewAppByDay(userId, period, year));
    }
}