package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.CandidateProfileRepository;
import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiCandidateProfileRepository")
public class CandidateProfileRepositoryImpl
        extends BaseRepositoryImpl<CandidateProfile, Long>
        implements CandidateProfileRepository {
    public CandidateProfileRepositoryImpl() {
        super(CandidateProfile.class);
    }
}
