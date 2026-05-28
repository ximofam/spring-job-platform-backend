package com.htweb.admin.repositories;

import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;

public interface EmployerProfileRepository extends BaseRepository<EmployerProfile,Long> {
    List<EmployerProfile> findAll();
}
