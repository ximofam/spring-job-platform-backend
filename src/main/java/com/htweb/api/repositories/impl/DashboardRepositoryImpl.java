package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.DashboardRepository;
import com.htweb.core.enums.ApplicationStatus;
import com.htweb.core.enums.Degree;
import com.htweb.core.pojo.*;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
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
    public List<Map<String, Object>> getProcessingSpeedReviewAppByDay(
            Long employerId, String period, Integer year) {

        var cb = getCurrentSession().getCriteriaBuilder();
        var cq = cb.createQuery(Object[].class);

        Root<ReviewApplication> r = cq.from(ReviewApplication.class);
        Join<ReviewApplication, Application> a = r.join("application", JoinType.INNER);
        Join<Application, Job> j = a.join("job", JoinType.INNER);

        Expression<Double> epochR = cb.function("date_part", Double.class, cb.literal("epoch"), r.get("createdAt"));
        Expression<Double> epochA = cb.function("date_part", Double.class, cb.literal("epoch"), a.get("createdAt"));
        Expression<Double> diffDays = cb.quot(cb.diff(epochR, epochA), cb.literal(86400.0)).as(Double.class);

        Expression<Double> avgApproved = cb.avg(
                cb.<Double>selectCase()
                        .when(cb.equal(r.get("newStatus"), ApplicationStatus.ACCEPTED), diffDays)
                        .otherwise(cb.nullLiteral(Double.class)));

        Expression<Double> avgRejected = cb.avg(
                cb.<Double>selectCase()
                        .when(cb.equal(r.get("newStatus"), ApplicationStatus.REJECTED), diffDays)
                        .otherwise(cb.nullLiteral(Double.class)));

        String datePart = period.equals("QUARTER") ? "quarter" : "month";
        Expression<Double> periodExpr = cb.function("date_part", Double.class,
                cb.literal(datePart), r.get("createdAt"));

        Expression<Double> yearExpr = cb.function("date_part", Double.class,
                cb.literal("year"), r.get("createdAt"));

        Subquery<Long> companySubq = cq.subquery(Long.class);
        Root<EmployerProfile> ep = companySubq.from(EmployerProfile.class);
        companySubq.select(ep.get("company").get("id"))
                .where(cb.equal(ep.get("id"), employerId));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(j.get("company").get("id").in(companySubq));
        predicates.add(cb.equal(r.get("oldStatus"), ApplicationStatus.PENDING));
        predicates.add(cb.equal(yearExpr, year.doubleValue())); // luôn lọc year

        cq.multiselect(periodExpr, avgApproved, avgRejected)
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(periodExpr)
                .orderBy(cb.asc(periodExpr));

        List<Object[]> rows = getCurrentSession().createQuery(cq).getResultList();

        // Fill đủ 12 tháng hoặc 4 quý, slot không có data → null
        int slots = period.equals("QUARTER") ? 4 : 12;
        Map<Integer, Object[]> rowMap = new LinkedHashMap<>();
        for (Object[] row : rows) {
            rowMap.put(((Number) row[0]).intValue(), row);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 1; i <= slots; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", i);
            if (rowMap.containsKey(i)) {
                Object[] row = rowMap.get(i);
                map.put("timeApprovedByDay", row[1]);
                map.put("timeRejectedByDay", row[2]);
            } else {
                map.put("timeApprovedByDay", null);
                map.put("timeRejectedByDay", null);
            }
            result.add(map);
        }
        return result;
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Map<String, Object>> getSalaryGapByJob(Long employerId) {

        var cb = getCurrentSession().getCriteriaBuilder();
        var cq = cb.createQuery(Object[].class);

        // FROM applications a JOIN jobs j
        Root<Application> a = cq.from(Application.class);
        Join<Application, Job> j = a.join("job", JoinType.INNER);
        Join<Job, Address> addr = j.join("address", JoinType.INNER);
        Join<Address, City> city = addr.join("city", JoinType.INNER);
        // (j.salaryMin + j.salaryMax) / 2  →  lương trung bình công ty
        Expression<Double> midSalary = cb.quot(
                cb.sum(j.get("salaryMin"), j.get("salaryMax")),
                cb.literal(2.0)).as(Double.class);

        // midSalary - a.expectedSalary  →  chênh lệch thô
        Expression<Double> gap = cb.diff(midSalary, a.get("expectedSalary"));

        // AVG(gap) / 1_000_000  →  đổi sang triệu VNĐ
        Expression<Double> avgGapMillion = cb.quot(
                cb.avg(gap),
                cb.literal(1_000_000.0)).as(Double.class);

        // ROUND(..., 1) — dùng hàm native PostgreSQL
        Expression<Double> rounded = cb.function(
                "round", Double.class,
                avgGapMillion.as(Double.class),
                cb.literal(1));

        // CONCAT(j.title, ' - ', j.city)
        Expression<String> nameExpr = cb.concat(
                cb.concat(j.get("title"), cb.literal(" - ")),
                city.get("name"));

        Subquery<Long> companySubq = cq.subquery(Long.class);
        Root<EmployerProfile> ep = companySubq.from(EmployerProfile.class);
        companySubq.select(ep.get("company").get("id"))
                .where(cb.equal(ep.get("id"), employerId));

        Predicate whereClause = j.get("company").get("id").in(companySubq);

        cq.multiselect(nameExpr, rounded)
                .where(whereClause)
                .groupBy(j.get("title"), city.get("name"))
                .orderBy(cb.asc(nameExpr));

        List<Object[]> rows = getCurrentSession().createQuery(cq).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", row[0]);
            map.put("gapSalary", row[1]);
            result.add(map);
        }
        return result;
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Map<String, Object> getCvOverviewMetrics(Long employerId, String period, Integer year) {

        var cb = getCurrentSession().getCriteriaBuilder();
        var cq = cb.createQuery(Object[].class);

        Root<Application> a = cq.from(Application.class);
        Join<Application, Job> j = a.join("job", JoinType.INNER);

        Expression<Integer> approvedCount = cb.sum(
                cb.<Integer>selectCase()
                        .when(cb.equal(a.get("status"), ApplicationStatus.ACCEPTED), cb.literal(1))
                        .otherwise(cb.literal(0)));

        Expression<Integer> rejectedCount = cb.sum(
                cb.<Integer>selectCase()
                        .when(cb.equal(a.get("status"), ApplicationStatus.REJECTED), cb.literal(1))
                        .otherwise(cb.literal(0)));

        Expression<Integer> pendingCount = cb.sum(
                cb.<Integer>selectCase()
                        .when(cb.equal(a.get("status"), ApplicationStatus.PENDING), cb.literal(1))
                        .otherwise(cb.literal(0)));

        Subquery<Long> companySubq = cq.subquery(Long.class);
        Root<EmployerProfile> ep = companySubq.from(EmployerProfile.class);
        companySubq.select(ep.get("company").get("id"))
                .where(cb.equal(ep.get("id"), employerId));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(j.get("company").get("id").in(companySubq));

        Expression<Double> dpYear = cb.function("date_part", Double.class,
                cb.literal("year"), a.get("createdAt"));

        // MONTH và YEAR đều lấy cả năm — chỉ QUARTER mới lọc thêm quý
        predicates.add(cb.equal(dpYear, year.doubleValue()));

        if (period.equals("QUARTER")) {
            Expression<Double> dpQuarter = cb.function("date_part", Double.class,
                    cb.literal("quarter"), a.get("createdAt"));
            // Tính quý hiện tại từ năm đang xét
            int currentQuarter = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
            predicates.add(cb.equal(dpQuarter, (double) currentQuarter));
        }


        cq.multiselect(cb.count(a), approvedCount, rejectedCount, pendingCount)
                .where(predicates.toArray(new Predicate[0]));

        Object[] row = getCurrentSession().createQuery(cq).getSingleResult();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalReceived", row[0]);
        result.put("approvedCount", row[1]);
        result.put("rejectedCount", row[2]);
        result.put("pendingCount", row[3]);
        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Map<String, Object>> getEducationQualityStats(
            Long employerId, String period, Integer year) {

        var cb = getCurrentSession().getCriteriaBuilder();
        var cq = cb.createQuery(Object[].class);

        Root<Application> a = cq.from(Application.class);
        Join<Application, Job> j = a.join("job", JoinType.INNER);
        Join<Application, CandidateProfile> cp = a.join("candidateProfile", JoinType.INNER);
        Join<CandidateProfile, Education> ed = cp.join("educations", JoinType.INNER);

        Subquery<Long> companySubq = cq.subquery(Long.class);
        Root<EmployerProfile> ep = companySubq.from(EmployerProfile.class);
        companySubq.select(ep.get("company").get("id"))
                .where(cb.equal(ep.get("id"), employerId));

        Subquery<Long> latestEduSubq = cq.subquery(Long.class);
        Root<Education> ed2 = latestEduSubq.from(Education.class);
        latestEduSubq.select(cb.max(ed2.get("id")))
                .where(cb.equal(ed2.get("candidateProfile").get("id"), cp.get("id")));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(j.get("company").get("id").in(companySubq));
        predicates.add(cb.equal(ed.get("id"), latestEduSubq));

        // Luôn lọc theo year
        Expression<Double> dpYear = cb.function("date_part", Double.class,
                cb.literal("year"), a.get("createdAt"));
        predicates.add(cb.equal(dpYear, year.doubleValue()));

        // QUARTER → lọc thêm quý hiện tại của năm đó
        if (period.equals("QUARTER")) {
            int currentQuarter = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
            Expression<Double> dpQuarter = cb.function("date_part", Double.class,
                    cb.literal("quarter"), a.get("createdAt"));
            predicates.add(cb.equal(dpQuarter, (double) currentQuarter));
        }

        Expression<Long> total = cb.count(a);
        Expression<Long> approvedCount = cb.count(
                cb.<Long>selectCase()
                        .when(cb.equal(a.get("status"), ApplicationStatus.ACCEPTED), a.get("id"))
                        .otherwise(cb.nullLiteral(Long.class)));

        cq.multiselect(ed.get("degree"), total, approvedCount)
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(ed.get("degree"))
                .orderBy(cb.asc(ed.get("degree")));

        List<Object[]> rows = getCurrentSession().createQuery(cq).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Degree degree = (Degree) row[0];
            long totalVal = ((Number) row[1]).longValue();
            long approved = ((Number) row[2]).longValue();
            double rate = totalVal > 0
                    ? Math.round((approved * 100.0 / totalVal) * 10.0) / 10.0
                    : 0.0;

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", degree.name());
            map.put("totalCv", totalVal);
            map.put("totalApproved", approved);
            map.put("rate", rate);
            result.add(map);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Map<String, Object>> getExperienceQualityStats(
            Long employerId, String period, Integer year) {

        var cb = getCurrentSession().getCriteriaBuilder();
        var cq = cb.createQuery(Object[].class);

        Root<Application> a = cq.from(Application.class);
        Join<Application, Job> j = a.join("job", JoinType.INNER);
        Join<Application, CandidateProfile> cp = a.join("candidateProfile", JoinType.INNER);

        // Company subquery
        Subquery<Long> companySubq = cq.subquery(Long.class);
        Root<EmployerProfile> ep = companySubq.from(EmployerProfile.class);
        companySubq.select(ep.get("company").get("id"))
                .where(cb.equal(ep.get("id"), employerId));

        // Subquery: số năm KN cao nhất = MAX(year(end) - year(start))
        Subquery<Integer> maxYearsSubq = cq.subquery(Integer.class);
        Root<Experience> ex = maxYearsSubq.from(Experience.class);

        Expression<LocalDate> endOrNow = cb.function(
                "COALESCE", LocalDate.class,
                ex.get("endDate"),
                cb.function("CURRENT_DATE", LocalDate.class)
        );
        Expression<Double> endYear = cb.function("date_part", Double.class,
                cb.literal("year"), endOrNow);
        Expression<Double> startYear = cb.function("date_part", Double.class,
                cb.literal("year"), ex.get("startDate"));
        Expression<Integer> diffYears = cb.diff(endYear, startYear).as(Integer.class);

        maxYearsSubq
                .select(cb.max(diffYears))
                .where(cb.equal(ex.get("candidateProfile").get("id"), cp.get("id")));

        // 0 nếu candidate không có experience
        Expression<Integer> expYears = cb.coalesce(maxYearsSubq, 0);

        // Predicates
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(j.get("company").get("id").in(companySubq));

        Expression<Double> dpYear = cb.function("date_part", Double.class,
                cb.literal("year"), a.get("createdAt"));
        predicates.add(cb.equal(dpYear, year.doubleValue()));

        if (period.equals("QUARTER")) {
            int currentQuarter = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
            Expression<Double> dpQuarter = cb.function("date_part", Double.class,
                    cb.literal("quarter"), a.get("createdAt"));
            predicates.add(cb.equal(dpQuarter, (double) currentQuarter));
        }

        // Aggregations
        Expression<Long> total = cb.count(a);
        Expression<Long> approvedCount = cb.count(
                cb.<Long>selectCase()
                        .when(cb.equal(a.get("status"), ApplicationStatus.ACCEPTED), a.get("id"))
                        .otherwise(cb.nullLiteral(Long.class)));

        cq.multiselect(expYears, total, approvedCount)
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(expYears)
                .orderBy(cb.asc(expYears));

        List<Object[]> rows = getCurrentSession().createQuery(cq).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            int yrs = ((Number) row[0]).intValue();
            long totalVal = ((Number) row[1]).longValue();
            long approved = ((Number) row[2]).longValue();
            double rate = totalVal > 0
                    ? Math.round((approved * 100.0 / totalVal) * 10.0) / 10.0
                    : 0.0;

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", yrs);
            map.put("totalCv", totalVal);
            map.put("totalApproved", approved);
            map.put("rate", rate);
            result.add(map);
        }
        return result;
    }
}

