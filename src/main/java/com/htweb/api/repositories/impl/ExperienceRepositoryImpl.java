package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.ExperienceRepository;
import com.htweb.core.pojo.Experience;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiExperienceRepository")
public class ExperienceRepositoryImpl extends BaseRepositoryImpl<Experience, Long> implements ExperienceRepository {
    public ExperienceRepositoryImpl() {
        super(Experience.class);
    }
}
