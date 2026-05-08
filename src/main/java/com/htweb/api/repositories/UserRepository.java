package com.htweb.api.repositories;

import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean isExistsUsername(String username);

    boolean isExistsEmail(String email);

    void createCandidateProfile(CandidateProfile profile);

    void createEmployerProfile(EmployerProfile profile);
}
