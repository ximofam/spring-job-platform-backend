package com.htweb.admin.repositories.Impl;

import com.htweb.admin.enums.StatisticPeriod;
import com.htweb.admin.repositories.StatisticsActivityRepository;
import com.htweb.core.enums.CompanyStatus;
import com.htweb.core.pojo.Company;
import com.htweb.core.pojo.User;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("adminStatisticsActivityRepository")
public class StatisticsActivityRepositoryImpl implements StatisticsActivityRepository {
    @Autowired
    private SessionFactory factory;

    protected Session getCurrentSession() {
        return factory.getCurrentSession();
    }

    private List<Predicate> buildPeriodPredicates(CriteriaBuilder cb, Expression<?> createdAt,
                                                  StatisticPeriod period, Integer value, Integer year) {

        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        List<Predicate> predicates = new ArrayList<>();

        Expression<Double> yearPart = cb.function("date_part", Double.class,
                cb.literal("year"), createdAt);
        Expression<Double> monthPart = cb.function("date_part", Double.class,
                cb.literal("month"), createdAt);
        Expression<Double> quarterPart = cb.function("date_part", Double.class,
                cb.literal("quarter"), createdAt);

        switch (period) {
            case MONTH -> {
                predicates.add(cb.equal(yearPart, (double) targetYear));
                if (value != null) predicates.add(cb.equal(monthPart, (double) value));
            }
            case QUARTER -> {
                predicates.add(cb.equal(yearPart, (double) targetYear));
                if (value != null) predicates.add(cb.equal(quarterPart, (double) value));
            }
            case YEAR -> predicates.add(cb.lessThanOrEqualTo(yearPart, (double) targetYear));
        }

        return predicates;
    }

    public long countPendingCompanies(StatisticPeriod period, Integer value, Integer year) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Company> root = cq.from(Company.class);

        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("status"), CompanyStatus.PENDING));

        Expression<Double> yearPart = cb.function("date_part", Double.class,
                cb.literal("year"), root.get("createdAt"));
        Expression<Double> monthPart = cb.function("date_part", Double.class,
                cb.literal("month"), root.get("createdAt"));
        Expression<Double> quarterPart = cb.function("date_part", Double.class,
                cb.literal("quarter"), root.get("createdAt"));

        switch (period) {
            case MONTH -> {
                predicates.add(cb.equal(yearPart, (double) targetYear));
                if (value != null) {
                    predicates.add(cb.equal(monthPart, (double) value));
                }
                // null → cả 12 tháng, chỉ cần filter year là đủ
            }

            case QUARTER -> {
                predicates.add(cb.equal(yearPart, (double) targetYear));
                if (value != null) {
                    // Đúng quý value (1–4)
                    predicates.add(cb.equal(quarterPart, (double) value));
                }
                // null → cả 4 quý, chỉ cần filter year là đủ
            }

            case YEAR -> {
                // Từ targetYear trở về trước
                predicates.add(cb.lessThanOrEqualTo(yearPart, (double) targetYear));
        }
        }
        cq.select(cb.count(root))
                .where(predicates.toArray(new Predicate[0]));

        return session.createQuery(cq).getSingleResult();
    }
    public Map<String, Long> countNewUsersByRole(StatisticPeriod period, Integer value, Integer year) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<User> root = cq.from(User.class);

        List<Predicate> predicates = buildPeriodPredicates(cb, root.get("createdAt"), period, value, year);

        cq.multiselect(root.get("userRole"), cb.count(root))
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(root.get("userRole"));

        Map<String, Long> result = session.createQuery(cq)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));

        result.putIfAbsent("CANDIDATE", 0L);
        result.putIfAbsent("EMPLOYER", 0L);
        return result;
    }
}




