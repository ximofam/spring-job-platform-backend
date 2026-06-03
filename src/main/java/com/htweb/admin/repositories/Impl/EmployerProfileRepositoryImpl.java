package com.htweb.admin.repositories.Impl;

import com.htweb.admin.repositories.EmployerProfileRepository;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.Permission;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("adminEmployerProfileRepositoryImpl")
public class EmployerProfileRepositoryImpl extends BaseRepositoryImpl<EmployerProfile, Long> implements EmployerProfileRepository {
    public EmployerProfileRepositoryImpl() {
        super(EmployerProfile.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<EmployerProfile> findAll() {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EmployerProfile> query = cb.createQuery(EmployerProfile.class);
        Root<EmployerProfile> root = query.from(EmployerProfile.class);

        root.fetch("user", JoinType.LEFT);
        root.fetch("company", JoinType.LEFT);

        query.select(root);

        return session.createQuery(query).getResultList();
    }
}
