package com.htweb.admin.repositories.Impl;

import com.htweb.admin.repositories.RoleRepository;
import com.htweb.core.pojo.Role;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends BaseRepositoryImpl<Role,Long> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }

}
