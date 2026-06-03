package com.htweb.admin.repositories.Impl;

import com.htweb.admin.repositories.RoleRepository;
import com.htweb.core.pojo.Role;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends BaseRepositoryImpl<Role, Long> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public Optional<Role> findByName(String name) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Role> query = cb.createQuery(Role.class);
        Root<Role> root = query.from(Role.class);

        query.select(root)
                .where(cb.equal(root.get("name"), name));

        return Optional.ofNullable(
                session.createQuery(query).uniqueResult()
        );
    }
}
