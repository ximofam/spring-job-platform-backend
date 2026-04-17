package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.Permission;
import com.htweb.core.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {
    private final SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public Set<Permission> findByRoleName(String roleName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Permission p JOIN p.roles r WHERE r.name = :roleName",
                        Permission.class
                ).setParameter("roleName", roleName)
                .getResultStream().collect(Collectors.toSet());
    }
}
