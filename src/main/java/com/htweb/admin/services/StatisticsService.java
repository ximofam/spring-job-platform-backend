package com.htweb.admin.services;

import com.htweb.admin.enums.StatisticPeriod;

import java.util.Map;

public interface StatisticsService {
    long countPendingCompanies(StatisticPeriod period, Integer value, Integer year);
    Map<String, Long> countNewUsersByRole(StatisticPeriod period, Integer value, Integer year);
}
