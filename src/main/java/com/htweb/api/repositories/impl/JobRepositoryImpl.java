package com.htweb.api.repositories.impl;

import com.htweb.api.dtos.job.JobSearchRequest;
import com.htweb.api.repositories.JobRepository;
import com.htweb.api.utils.Utils;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Job;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("apiJobRepository")
public class JobRepositoryImpl extends BaseRepositoryImpl<Job, Long> implements JobRepository {
    public JobRepositoryImpl() {
        super(Job.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateResponse<Job> searchJobs(JobSearchRequest request) {
        Session session = getCurrentSession();
        int page = request.getPage();
        int size = request.getSize();

        StringBuilder fromClause = new StringBuilder("FROM jobs j");
        if (request.getDistrictId() != null) {
            fromClause.append(" JOIN addresses a ON a.id = j.address_id");
        }

        StringBuilder whereClause = new StringBuilder("WHERE j.deleted_at IS NULL AND j.status = 'PUBLISHED'");
        Map<String, Object> params = new HashMap<>();

        if (Utils.hasText(request.getQ())) {
            whereClause.append(" AND j.search_vector @@ websearch_to_tsquery('simple', unaccent(:q))");
            params.put("q", request.getQ().trim());
        }
        if (Utils.hasText(request.getEmploymentType())) {
            whereClause.append(" AND j.employment_type = :employmentType");
            params.put("employmentType", request.getEmploymentType());
        }
        if (Utils.hasText(request.getExperienceLevel())) {
            whereClause.append(" AND j.experience_level = :experienceLevel");
            params.put("experienceLevel", request.getExperienceLevel());
        }
        if (request.getCategoryId() != null) {
            whereClause.append(" AND j.category_id = :categoryId");
            params.put("categoryId", request.getCategoryId());
        }
        if (request.getSalaryMin() != null) {
            whereClause.append(" AND j.salary_max >= :salaryMin");
            params.put("salaryMin", request.getSalaryMin());
        }
        if (request.getSalaryMax() != null) {
            whereClause.append(" AND j.salary_min <= :salaryMax");
            params.put("salaryMax", request.getSalaryMax());
        }
        if (request.getDistrictId() != null) {
            whereClause.append(" AND a.district_id = :districtId");
            params.put("districtId", request.getDistrictId());
        }

        String orderBy = Utils.hasText(request.getQ())
                ? "ORDER BY ts_rank_cd(j.search_vector, websearch_to_tsquery('simple', unaccent(:q)), 4) DESC, j.boost_score DESC"
                : "ORDER BY j.boost_score DESC, j.published_at DESC";

        String sql = "SELECT j.* " + fromClause + " " + whereClause + " " + orderBy;
        String countSql = "SELECT COUNT(*) " + fromClause + " " + whereClause;

        NativeQuery<Long> countQuery = session.createNativeQuery(countSql, Long.class);
        NativeQuery<Job> dataQuery = session.createNativeQuery(sql, Job.class);
        params.forEach((k, v) -> {
            countQuery.setParameter(k, v);
            dataQuery.setParameter(k, v);
        });

        long totalElements = countQuery.uniqueResult();
        List<Job> data = dataQuery
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

        if (!data.isEmpty()) {
            List<Long> jobIds = data.stream()
                    .map(Job::getId)
                    .toList();

            session.createQuery(
                            "SELECT j FROM Job j LEFT JOIN FETCH j.company WHERE j.id IN :ids",
                            Job.class)
                    .setParameter("ids", jobIds)
                    .getResultList();

            session.createQuery(
                            "SELECT j FROM Job j LEFT JOIN FETCH j.address a LEFT JOIN FETCH a.city LEFT JOIN FETCH a.district WHERE j.id IN :ids",
                            Job.class)
                    .setParameter("ids", jobIds)
                    .getResultList();
        }

        return PaginateResponse.<Job>builder()
                .data(data)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .build();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isJobBelongToCompany(Long jobId, Long companyId) {
        String hql = """
                SELECT count(j.id)
                FROM Job j
                WHERE j.id = :jobId
                  AND j.company.id = :companyId
                """;

        Long count = getCurrentSession().createQuery(hql, Long.class)
                .setParameter("jobId", jobId)
                .setParameter("companyId", companyId)
                .getSingleResult();

        return count != null && count > 0;
    }

}
