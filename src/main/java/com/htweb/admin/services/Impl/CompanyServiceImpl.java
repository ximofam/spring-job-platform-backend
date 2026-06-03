package com.htweb.admin.services.Impl;

import com.htweb.admin.repositories.CompanyRepository;
import com.htweb.admin.services.CompanyService;
import com.htweb.core.pojo.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company update(Company entity) {
        return companyRepository.update(entity);
    }
}