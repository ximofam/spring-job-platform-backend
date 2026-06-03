package com.htweb.admin.repositories.Impl;

import com.htweb.admin.repositories.PermissionRepository;
import com.htweb.core.pojo.Permission;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("adminPermissionRepositoryImpl")
public class PermissionRepositoryImpl extends BaseRepositoryImpl<Permission,Long> implements PermissionRepository {
    public PermissionRepositoryImpl() {
        super(Permission.class);
    }

    @Override
    public List<Permission> findAllById(List<Long> ids) {
        return this.getCurrentSession().createQuery(
                "SELECT p FROM Permission p WHERE p.id IN :ids", Permission.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}