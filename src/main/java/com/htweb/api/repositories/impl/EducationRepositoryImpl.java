package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.EducationRepository;
import com.htweb.core.pojo.Education;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiEducationRepository")
public class EducationRepositoryImpl extends BaseRepositoryImpl<Education, Long> implements EducationRepository {
    public EducationRepositoryImpl() {
        super(Education.class);
    }
}
