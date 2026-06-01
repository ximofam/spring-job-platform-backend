package com.htweb.api.repositories;

import java.util.List;
import java.util.Map;

public interface DashboardRepository {
    List<Map<String, Object>> getProcessingSpeedReviewAppByDay(Long employerId, String period, Integer year);
}
