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
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class ApiDashboardController {

    @Qualifier("apiDashboardService")
    private final DashboardService dashboardService;

    @GetMapping("/process-speed")
    public ResponseEntity<?> getProcessingSpeed(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "2024") Integer year
    ) {
        return ResponseEntity.ok(dashboardService.getProcessingSpeedReviewAppByDay(userId, period, year));
    }

    @GetMapping("/salary-gap-job")
    public ResponseEntity<?> getSalaryGapByJob(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(dashboardService.getSalaryGapByJob(userId));
    }

    @GetMapping("/total-cv")
    public ResponseEntity<?> getCvOverviewMetrics(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "2024") Integer year
    ) {
        return ResponseEntity.ok(dashboardService.getCvOverviewMetrics(userId,period,year));
    }

    @GetMapping("/edu-quality")
    public ResponseEntity<?> getEducationQualityStats(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "2024") Integer year
    ) {
        return ResponseEntity.ok(dashboardService.getEducationQualityStats(userId,period,year));
    }

    @GetMapping("/exp-quality")
    public ResponseEntity<?> getExperienceQualityStats(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "2024") Integer year
    ) {
        return ResponseEntity.ok(dashboardService.getExperienceQualityStats(userId,period,year));
    }

}