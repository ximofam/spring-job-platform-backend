package com.htweb.admin.services;

import com.htweb.core.pojo.EmployerProfile;

import java.util.List;
import java.util.Optional;

public interface EmployerProfileService {
     EmployerProfile update(EmployerProfile entity);
     EmployerProfile findById(Long id);

    List<EmployerProfile> findAll();
}
