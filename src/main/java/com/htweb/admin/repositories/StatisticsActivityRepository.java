package com.htweb.admin.repositories;

import com.htweb.admin.enums.StatisticPeriod;

import java.util.List;
import java.util.Map;

public interface StatisticsActivityRepository {
    long countPendingCompanies(StatisticPeriod period, Integer value, Integer year);
    Map<String, Long> countNewUsersByRole(StatisticPeriod period, Integer value, Integer year);
    List<Map<String, Object>> countNewUsersByRoleGrouped(StatisticPeriod period, Integer year);
    Map<String, Long> countCompaniesByStatus(StatisticPeriod period, Integer value, Integer year);
    List<Map<String, Object>> countNewJobsGrouped(StatisticPeriod period, Integer year);

    List<Map<String, Object>> countJobsByCategory(StatisticPeriod period, Integer value, Integer year);
    Map<String, Long> countApplicationsByStatus(StatisticPeriod period, Integer value, Integer year);

    List<Map<String, Object>> revenueGrouped(StatisticPeriod period, Integer year);
    List<Map<String, Object>> topCompaniesByRevenue(StatisticPeriod period, Integer year);
}
