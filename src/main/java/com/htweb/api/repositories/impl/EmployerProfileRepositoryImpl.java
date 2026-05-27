package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.EmployerProfileRepository;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiEmployerProfileRepository")
public class EmployerProfileRepositoryImpl
        extends BaseRepositoryImpl<EmployerProfile, Long>
        implements EmployerProfileRepository {
    public EmployerProfileRepositoryImpl() {
        super(EmployerProfile.class);
    }
}
