package com.htweb.admin.repositories;

import com.htweb.admin.enums.StatisticPeriod;

import java.util.Map;

public interface StatisticsActivityRepository {
    long countPendingCompanies(StatisticPeriod period, Integer value, Integer year);
    Map<String, Long> countNewUsersByRole(StatisticPeriod period, Integer value, Integer year)
}
