package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.Permission;
import com.htweb.core.repositories.PermissionRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PermissionRepositoryImpl extends BaseRepositoryImpl<Permission, Long> implements PermissionRepository {
    public PermissionRepositoryImpl() {
        super(Permission.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Permission> findByRoleName(String roleName) {
        Session session = this.getCurrentSession();
        return session.createQuery(
                        "FROM Permission p JOIN p.roles r WHERE r.name = :roleName",
                        Permission.class)
                .setParameter("roleName", roleName)
                .getResultStream().collect(Collectors.toSet());
    }
}
