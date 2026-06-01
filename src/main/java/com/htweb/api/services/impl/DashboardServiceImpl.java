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

        String dbPeriod = switch (period) {
            case "month"   -> "MONTH";
            case "quarter" -> "QUARTER";
            default        -> null;
        };

        return dashboardRepository.getProcessingSpeedReviewAppByDay(userId, dbPeriod, year);
    }
}
