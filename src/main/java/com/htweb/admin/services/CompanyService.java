package com.htweb.admin.services;

import com.htweb.core.pojo.Company;

import java.util.Optional;

public interface CompanyService {
    Optional<Company> findById(Long id);
    Company update(Company entity);
}
