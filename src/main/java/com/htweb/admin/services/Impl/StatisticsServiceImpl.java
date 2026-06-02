package com.htweb.admin.services.Impl;

import com.htweb.admin.enums.StatisticPeriod;
import com.htweb.admin.repositories.StatisticsActivityRepository;
import com.htweb.admin.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("adminStatisticsService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

    @Qualifier("adminStatisticsActivityRepository")
    private final StatisticsActivityRepository activityRepository;

    @Override
    public long countPendingCompanies(StatisticPeriod period, Integer value, Integer year) {
        return activityRepository.countPendingCompanies(period, value, year);
    }

    @Override
    public Map<String, Long> countNewUsersByRole(StatisticPeriod period, Integer value, Integer year) {
        return activityRepository.countNewUsersByRole(period,value,year);
    }
}
