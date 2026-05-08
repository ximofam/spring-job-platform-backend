package com.htweb.api.repositories;

import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.repositories.BaseRepository;

import java.util.Optional;

public interface CandidateProfileRepository extends BaseRepository<CandidateProfile, Long> {
    Optional<CandidateProfile> findByUserId(Long userId);
}
