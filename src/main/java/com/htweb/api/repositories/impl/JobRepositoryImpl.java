package com.htweb.api.repositories.impl;

import com.htweb.api.dtos.job.JobComparationResponse;
import com.htweb.api.dtos.job.JobSearchRequest;
import com.htweb.api.repositories.JobRepository;
import com.htweb.api.utils.Utils;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Job;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import groovy.lang.Tuple;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("apiJobRepository")
public class JobRepositoryImpl extends BaseRepositoryImpl<Job, Long> implements JobRepository {
    public JobRepositoryImpl() {
        super(Job.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateResponse<Job> searchJobs(JobSearchRequest request, float[] queryVector) {
        Session session = getCurrentSession();
        int page = request.getPage();
        int size = request.getSize();

        String vectorStr = null;
        if (queryVector != null && queryVector.length > 0) {
            vectorStr = Arrays.toString(queryVector);
        }

        if (vectorStr != null) {
            PaginateResponse<Job> vectorResult = executeSearch(session, request, vectorStr, page, size);
            if (vectorResult.getTotalElements() > 0) {
                return vectorResult;
            }
        }

        return executeSearch(session, request, null, page, size);
    }

    private PaginateResponse<Job> executeSearch(Session session, JobSearchRequest request,
                                                String vectorStr, int page, int size) {
        StringBuilder fromClause = new StringBuilder("FROM jobs j");
        if (request.getDistrictId() != null) {
            fromClause.append(" JOIN addresses a ON a.id = j.address_id");
        }

        StringBuilder whereClause = new StringBuilder("WHERE j.deleted_at IS NULL AND j.status = 'PUBLISHED'");
        Map<String, Object> params = new HashMap<>();

        if (vectorStr != null) {
            whereClause.append(" AND (j.embedding <=> CAST(:queryVector AS vector)) <= 0.5");
            params.put("queryVector", vectorStr);
        } else if (Utils.hasText(request.getQ())) {
            whereClause.append(" AND j.title ILIKE :q");
            params.put("q", "%" + request.getQ().trim() + "%");
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

        String orderBy = (vectorStr != null)
                ? "ORDER BY (j.embedding <=> CAST(:queryVector AS vector)) ASC, j.boost_score DESC"
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
        if (totalElements == 0) {
            return PaginateResponse.empty(page, size);
        }

        List<Job> data = dataQuery
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();


        List<Long> jobIds = data.stream().map(Job::getId).toList();

        session.createQuery("SELECT j FROM Job j LEFT JOIN FETCH j.company WHERE j.id IN :ids", Job.class)
                .setParameter("ids", jobIds)
                .getResultList();

        session.createQuery("SELECT j FROM Job j LEFT JOIN FETCH j.address a LEFT JOIN FETCH a.city LEFT JOIN FETCH a.district WHERE j.id IN :ids", Job.class)
                .setParameter("ids", jobIds)
                .getResultList();


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

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<String> suggestKeywords(String query) {
        if (query == null || query.trim().length() < 2) {
            return Collections.emptyList();
        }

        String cleanQuery = query.trim();
        Session session = getCurrentSession();


        String sql = """
                SELECT title
                FROM jobs
                WHERE deleted_at IS NULL
                    AND status = 'PUBLISHED'
                    AND (title % :keyword OR title ILIKE :prefixKeyword)
                GROUP BY title
                ORDER BY
                    (title ILIKE :prefixKeyword) DESC,
                similarity(title, :keyword) DESC
                LIMIT 10
                """;

        return session.createNativeQuery(sql, String.class)
                .setParameter("keyword", cleanQuery)
                .setParameter("prefixKeyword", cleanQuery + "%")
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<JobComparationResponse> findMatchScores(List<Long> jobIds, float[] candidateVector) {
        if (jobIds == null || jobIds.isEmpty() || candidateVector == null) {
            return Collections.emptyList();
        }

        String vectorStr = Arrays.toString(candidateVector);

        String sql = """
            SELECT
                j.id    AS job_id,
                j.title AS title,
                ROUND(
                    CAST((1 - (j.embedding <=> CAST(:vector AS vector))) * 100 AS numeric)
                , 1)    AS score
            FROM jobs j
            WHERE j.id = ANY(:jobIds)
              AND j.embedding IS NOT NULL
              AND j.deleted_at IS NULL
            ORDER BY score DESC
            """;

        return getCurrentSession().createNativeQuery(sql, Object[].class)
                .setParameter("vector", vectorStr)
                .setParameter("jobIds", jobIds.toArray(new Long[0]))  // fix ở đây
                .getResultList()
                .stream()
                .map(row -> new JobComparationResponse(
                        ((Number) row[0]).longValue(),  // job_id
                        (String) row[1],                // title
                        ((Number) row[2]).doubleValue() // score
                ))
                .toList();
    }

}
