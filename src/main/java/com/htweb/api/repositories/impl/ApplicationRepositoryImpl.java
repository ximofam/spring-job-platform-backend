package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.ApplicationRepository;
import com.htweb.core.pojo.Application;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("apiApplicationRepository")
public class ApplicationRepositoryImpl extends BaseRepositoryImpl<Application, Long> implements ApplicationRepository {
    public ApplicationRepositoryImpl() {
        super(Application.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Application> findApplicationOfCandidate(Long candidateId) {
        String hql = """
                FROM Application a
                JOIN FETCH a.job
                WHERE a.candidateProfile.id = :candidateId
                """;

        return getCurrentSession().createQuery(hql, Application.class)
                .setParameter("candidateId", candidateId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Application> findApplicationOfEmployer(Long employerId) {
        String hql = """
                SELECT a FROM Application a
                JOIN FETCH a.job j
                JOIN FETCH a.candidateProfile cp
                JOIN FETCH cp.user u
                WHERE j.company.id IN (
                    SELECT ep.company.id FROM EmployerProfile ep WHERE ep.id = :employerId)
                """;

        return getCurrentSession().createQuery(hql, Application.class)
                .setParameter("employerId", employerId)
                .getResultList();
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isRelateToUser(Long applicationId, Long userId) {
        String hql = """
                SELECT COUNT(a) > 0
                FROM Application a
                LEFT JOIN a.job j
                WHERE a.id = :applicationId
                AND (
                    a.candidateProfile.id = :userId
                    OR j.company.id IN (
                        SELECT ep.company.id
                        FROM EmployerProfile ep
                        WHERE ep.id = :userId
                    )
                )
                """;

        return getCurrentSession().createQuery(hql, Boolean.class)
                .setParameter("applicationId", applicationId)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
