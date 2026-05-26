package com.htweb.admin.services;

import com.htweb.core.pojo.EmployerProfile;

import java.util.Optional;

public interface EmployerProfileService {
    public EmployerProfile update(EmployerProfile entity);
    public Optional<EmployerProfile> findById(Long id);
}
