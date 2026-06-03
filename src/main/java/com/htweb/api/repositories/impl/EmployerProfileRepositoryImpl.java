package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.EmployerProfileRepository;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("apiEmployerProfileRepository")
public class EmployerProfileRepositoryImpl
        extends BaseRepositoryImpl<EmployerProfile, Long>
        implements EmployerProfileRepository {
    public EmployerProfileRepositoryImpl() {
        super(EmployerProfile.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<EmployerProfile> findAndFetchUserById(Long id) {
        String hql = "FROM EmployerProfile ep JOIN FETCH ep.user WHERE ep.id = :id";

        return getCurrentSession().createQuery(hql, EmployerProfile.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<EmployerProfile> findByJobId(Long jobId) {
        String hql = """
                    SELECT ep FROM EmployerProfile ep
                    WHERE ep.company.id = (
                        SELECT j.company.id
                        FROM Job j
                        WHERE j.id = :jobId
                    )
                """;

        return getCurrentSession().createQuery(hql, EmployerProfile.class)
                .setParameter("jobId", jobId)
                .uniqueResultOptional();
    }
}
