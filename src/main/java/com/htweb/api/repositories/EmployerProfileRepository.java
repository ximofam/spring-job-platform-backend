package com.htweb.api.repositories;

import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.repositories.BaseRepository;

import java.util.Optional;

public interface EmployerProfileRepository extends BaseRepository<EmployerProfile, Long> {
    Optional<EmployerProfile> findAndFetchUserById(Long id);
}
