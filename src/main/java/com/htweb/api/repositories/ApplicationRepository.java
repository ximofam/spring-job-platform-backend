package com.htweb.api.repositories;

import com.htweb.core.pojo.Application;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;

public interface ApplicationRepository extends BaseRepository<Application, Long> {
    List<Application> findApplicationOfCandidate(Long candidateId);

    List<Application> findApplicationOfEmployer(Long employerId);

    boolean isRelateToUser(Long applicationId, Long userId);
}
