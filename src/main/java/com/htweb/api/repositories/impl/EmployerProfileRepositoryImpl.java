package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.EmployerProfileRepository;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.hibernate.Session;
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
    public Optional<EmployerProfile> findById(Long userId) {
        Session session = this.getCurrentSession();
        String hql = """
                FROM EmployerProfile p LEFT JOIN FETCH p.company WHERE p.userId = :userId
                """;

        return session.createQuery(hql, EmployerProfile.class)
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<EmployerProfile> findByUserId(Long userId) {
        String hql = "FROM EmployerProfile p WHERE p.userId = :userId";
        
        return getCurrentSession().createQuery(hql, EmployerProfile.class)
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }
}
