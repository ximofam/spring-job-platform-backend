package com.htweb.admin.repositories.Impl;

import com.htweb.admin.repositories.EmployerProfileRepository;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.Permission;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;

public class EmployerProfileRepositoryImpl extends BaseRepositoryImpl<EmployerProfile, Long> implements EmployerProfileRepository {
    public EmployerProfileRepositoryImpl() {
        super(EmployerProfile.class);
    }

}
