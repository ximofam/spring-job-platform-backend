package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.Permission;
import com.htweb.core.repositories.PermissionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PermissionRepositoryImpl implements PermissionRepository {
    @Autowired
    private SessionFactory factory;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Set<Permission> findByRoleName(String roleName) {
        Session session = factory.getCurrentSession();
        return session.createQuery(
                        "FROM Permission p JOIN p.roles r WHERE r.name = :roleName",
                        Permission.class)
                .setParameter("roleName", roleName)
                .getResultStream().collect(Collectors.toSet());
    }
}
