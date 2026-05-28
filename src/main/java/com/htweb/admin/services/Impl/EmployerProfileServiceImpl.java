package com.htweb.admin.services.Impl;

import com.htweb.admin.repositories.EmployerProfileRepository;
import com.htweb.admin.services.EmployerProfileService;
import com.htweb.core.pojo.EmployerProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("adminEmployerProfileServiceImpl")
@RequiredArgsConstructor
public class EmployerProfileServiceImpl implements EmployerProfileService {
    @Qualifier("adminEmployerProfileRepositoryImpl")
    private final EmployerProfileRepository employerProfileRepository;
    @Override
    public EmployerProfile update(EmployerProfile entity) {
        return this.employerProfileRepository.update(entity);
    }

    @Override
    public EmployerProfile findById(Long id) {
        return this.employerProfileRepository.findById(id).orElseThrow();
    }

    @Override
    public List<EmployerProfile> findAll() {
        return this.employerProfileRepository.findAll();
    }
}
