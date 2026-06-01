package com.htweb.api.services;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    List<Map<String, Object>> getProcessingSpeedReviewAppByDay(Long userId, String period, Integer year);
    List<Map<String, Object>> getSalaryGapByJob(Long employerId);
    Map<String, Object> getCvOverviewMetrics(Long employerId, String period, Integer year);
    List<Map<String, Object>> getEducationQualityStats(Long employerId, String period, Integer year);
    List<Map<String, Object>> getExperienceQualityStats(
            Long employerId, String period, Integer year);
}
