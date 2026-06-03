package com.htweb.admin.services;

import com.htweb.admin.enums.StatisticPeriod;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    long countPendingCompanies(StatisticPeriod period, Integer value, Integer year);
    Map<String, Object> countCompaniesByStatus(StatisticPeriod period, Integer value, Integer year);
    Map<String, Object> countNewJobsGrouped(StatisticPeriod period, Integer year);
    Map<String, Long> countNewUsersByRole(StatisticPeriod period, Integer value, Integer year);
    Map<String, Object> countNewUsersByRoleGrouped(StatisticPeriod period, Integer year);

    Map<String, Object> countJobsByCategory(StatisticPeriod period,Integer value, Integer year);
    Map<String, Object> countApplicationsByStatus(StatisticPeriod period, Integer value, Integer year);

    Map<String, Object> revenueGrouped(StatisticPeriod period, Integer year);
    List<Map<String, Object>> topCompaniesByRevenue(StatisticPeriod period, Integer year);
}
