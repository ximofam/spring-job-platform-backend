package com.htweb.api.repositories;

import com.htweb.core.pojo.CandidateCv;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;

public interface CandidateCvRepository extends BaseRepository<CandidateCv, Long> {
    List<CandidateCv> findByUserId(Long userId);
}
