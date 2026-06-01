package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.DashboardRepository;
import com.htweb.core.enums.ApplicationStatus;
import com.htweb.core.pojo.Application;
import com.htweb.core.pojo.Job;
import com.htweb.core.pojo.ReviewApplication;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("apiDashboardRepository")
public class DashboardRepositoryImpl implements DashboardRepository {

    @Autowired
    private SessionFactory factory;

    protected Session getCurrentSession() {
        return factory.getCurrentSession();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Map<String, Object>> getProcessingSpeedReviewAppByDay(Long employerId, String period, Integer year) {

        String periodExpr = switch (period) {
            case "MONTH" -> "month(r.createdAt)";
            case "QUARTER" -> "quarter(r.createdAt)";
            default -> "year(r.createdAt)";
        };

        String yearCondition = period.equals("YEAR") ? "" : "AND year(r.createdAt) = :year";

        String hql = """
        SELECT %s,
            AVG(CASE WHEN r.newStatus = :approved
                THEN (extract(epoch from r.createdAt) - extract(epoch from a.createdAt)) / 86400.0
                ELSE NULL END),
            AVG(CASE WHEN r.newStatus = :rejected
                THEN (extract(epoch from r.createdAt) - extract(epoch from a.createdAt)) / 86400.0
                ELSE NULL END)
        FROM ReviewApplication r
        JOIN r.application a
        JOIN a.job j
        WHERE j.company.id IN (
            SELECT ep.company.id FROM EmployerProfile ep WHERE ep.id = :employerId
        )
          AND r.oldStatus = :pending
          %s
        GROUP BY %s
        ORDER BY %s ASC
    """.formatted(periodExpr, yearCondition, periodExpr, periodExpr);

        var query = getCurrentSession()
                .createQuery(hql, Object[].class)
                .setParameter("employerId", employerId)
                .setParameter("approved", ApplicationStatus.ACCEPTED)
                .setParameter("rejected", ApplicationStatus.REJECTED)
                .setParameter("pending",  ApplicationStatus.PENDING);

        if (!period.equals("YEAR")) {
            query.setParameter("year", year);
        }

        List<Object[]> rows = query.getResultList();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", row[0]);
            map.put("Thời gian Duyệt (ngày)", row[1]);
            map.put("Thời gian Từ chối (ngày)", row[2]);
            result.add(map);
        }
        return result;
    }
}