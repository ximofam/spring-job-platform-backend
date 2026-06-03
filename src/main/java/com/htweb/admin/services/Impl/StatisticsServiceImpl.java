package com.htweb.admin.services.Impl;

import com.htweb.admin.enums.StatisticPeriod;
import com.htweb.admin.repositories.StatisticsActivityRepository;
import com.htweb.admin.services.StatisticsService;
import com.htweb.core.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        return activityRepository.countNewUsersByRole(period, value, year);
    }

    @Override
    public Map<String, Object> countNewUsersByRoleGrouped(
            StatisticPeriod period,
            Integer year
    ) {
        List<Map<String, Object>> rows =
                activityRepository.countNewUsersByRoleGrouped(period, year);

        List<Integer> yearLabels = null;
        int maxPeriod;

        if (period == StatisticPeriod.YEAR) {
            yearLabels = rows.stream()
                    .map(row -> ((Number) row.get("period")).intValue())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            maxPeriod = yearLabels.size();
        } else {
            maxPeriod = switch (period) {
                case MONTH -> 12;
                case QUARTER -> 4;
                default -> 0;
            };
        }

        List<String> labels = new ArrayList<>();
        if (period == StatisticPeriod.MONTH) {
            for (int i = 1; i <= 12; i++) labels.add("T" + i);

        } else if (period == StatisticPeriod.QUARTER) {
            for (int i = 1; i <= 4; i++) labels.add("Q" + i);

        } else if (period == StatisticPeriod.YEAR) {
            for (int y : yearLabels) labels.add(String.valueOf(y));
        }

        Map<String, List<Long>> roleData = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            int periodValue = ((Number) row.get("period")).intValue();
            String role = String.valueOf(row.get("role"));
            long count = ((Number) row.get("count")).longValue();

            roleData.putIfAbsent(role,
                    new ArrayList<>(Collections.nCopies(maxPeriod, 0L)));

            int index = (period == StatisticPeriod.YEAR)
                    ? yearLabels.indexOf(periodValue)
                    : periodValue - 1;
            roleData.get(role).set(index, count);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();
        roleData.forEach((role, data) -> {
            Map<String, Object> dataset = new HashMap<>();
            dataset.put("label", role);
            dataset.put("data", data);
            datasets.add(dataset);
        });

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);
        return result;
    }


    @Override
    public Map<String, Object> countCompaniesByStatus(StatisticPeriod period, Integer value, Integer year) {
        Map<String, Long> stats = activityRepository.countCompaniesByStatus(period, value, year);
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        stats.forEach((status, count) -> {
            labels.add(status);
            data.add(count);
        });
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    @Override
    public Map<String, Object> countNewJobsGrouped(StatisticPeriod period, Integer year) {

        List<Map<String, Object>> rows = activityRepository.countNewJobsGrouped(period, year);

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Number periodValue = (Number) row.get("period");
            Number countValue = (Number) row.get("count");
            String label;
            switch (period) {
                case MONTH:
                    label = "T" + periodValue.intValue();
                    break;
                case QUARTER:
                    label = "Q" + periodValue.intValue();
                    break;
                case YEAR:
                    label = String.valueOf(periodValue.intValue());
                    break;
                default:
                    label = String.valueOf(periodValue.intValue());
            }
            labels.add(label);
            data.add(countValue.longValue());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }


    @Override
    public Map<String, Object> countJobsByCategory(StatisticPeriod period, Integer value, Integer year) {
        List<Map<String, Object>> rows = activityRepository.countJobsByCategory(period, value, year);

        List<String> labels = new ArrayList<>();
        List<Long>   data   = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            labels.add(String.valueOf(row.get("category")));
            data.add(((Number) row.get("count")).longValue());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }
    @Override
    public Map<String, Object> countApplicationsByStatus(StatisticPeriod period, Integer value, Integer year) {
        Map<String, Long> stats = activityRepository.countApplicationsByStatus(period, value, year);

        // Sửa lại cho đúng với ApplicationStatus enum của bạn
        List<String> ORDER = List.of("PENDING", "REVIEWING", "ACCEPTED", "REJECTED");

        List<String> labels = new ArrayList<>();
        List<Long>   data   = new ArrayList<>();

        for (ApplicationStatus status : ApplicationStatus.values()) {
            labels.add(status.name());
            data.add(stats.getOrDefault(status.name(), 0L));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    @Override
    public Map<String, Object> revenueGrouped(StatisticPeriod period, Integer year) {
        List<Map<String, Object>> rows = activityRepository.revenueGrouped(period, year);

        List<String> labels = new ArrayList<>();
        List<Long>   data   = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            int periodValue = ((Number) row.get("period")).intValue();
            long revenue    = ((Number) row.get("revenue")).longValue();

            String label = switch (period) {
                case MONTH   -> "T" + periodValue;
                case QUARTER -> "Q" + periodValue;
                case YEAR    -> String.valueOf(periodValue);
            };

            labels.add(label);
            data.add(revenue);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    @Override
    public List<Map<String, Object>> topCompaniesByRevenue(StatisticPeriod period, Integer year) {
        return activityRepository.topCompaniesByRevenue(period, year);
    }


}
