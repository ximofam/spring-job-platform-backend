package com.htweb.admin.services.Impl;

import com.htweb.admin.services.EmployerProfileService;
import com.htweb.api.repositories.EmployerProfileRepository;
import com.htweb.core.pojo.EmployerProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class EmployerProfileServiceImpl implements EmployerProfileService {
    private final EmployerProfileRepository employerProfileRepository;
    @Override
    public EmployerProfile update(EmployerProfile entity) {
        return this.employerProfileRepository.update(entity);
    }

    @Override
    public Optional<EmployerProfile> findById(Long id) {
        return this.employerProfileRepository.findById(id);
    }
}
