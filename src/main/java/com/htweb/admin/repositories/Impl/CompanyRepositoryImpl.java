package com.htweb.admin.repositories.Impl;

import com.htweb.admin.repositories.CompanyRepository;
import com.htweb.core.pojo.Company;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepositoryImpl extends BaseRepositoryImpl<Company, Long>
        implements CompanyRepository {

    public CompanyRepositoryImpl() {
        super(Company.class);
    }
}
