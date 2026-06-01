package com.htweb.api.services.impl;

import com.htweb.api.repositories.DashboardRepository;
import com.htweb.api.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("apiDashboardService")
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {


    @Qualifier("apiDashboardRepository")
    private final DashboardRepository dashboardRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProcessingSpeedReviewAppByDay(Long userId, String period, Integer year) {

        if (period == null) {
            throw new IllegalArgumentException("period is required");
        }

        String dbPeriod = switch (period) {
            case "month"   -> "MONTH";
            case "quarter" -> "QUARTER";
            default        -> "null";
        };

        return dashboardRepository.getProcessingSpeedReviewAppByDay(userId, dbPeriod, year);
    }

    @Override
    public List<Map<String, Object>> getSalaryGapByJob(Long employerId) {
        return dashboardRepository.getSalaryGapByJob(employerId);
    }

    public Map<String, Object> getCvOverviewMetrics(Long employerId, String period, Integer year){
        if (period == null) {
            throw new IllegalArgumentException("period is required");
        }

        String dbPeriod = switch (period) {
            case "month"   -> "MONTH";
            case "quarter" -> "QUARTER";
            default        -> "null";
        };
        return dashboardRepository.getCvOverviewMetrics(employerId,dbPeriod,year);
    }

    @Override
    public List<Map<String, Object>> getEducationQualityStats(Long employerId, String period, Integer year) {
        return dashboardRepository.getEducationQualityStats(employerId, period, year);
    }

    @Override
    public List<Map<String, Object>> getExperienceQualityStats(Long employerId, String period, Integer year) {
        return dashboardRepository.getExperienceQualityStats(employerId, period, year);
    }


}
