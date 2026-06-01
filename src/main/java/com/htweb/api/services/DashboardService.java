package com.htweb.api.services;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    List<Map<String, Object>> getProcessingSpeedReviewAppByDay(Long userId, String period, Integer year);
}
