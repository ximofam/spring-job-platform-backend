package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.ReviewApplicationRepository;
import com.htweb.core.pojo.ReviewApplication;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiReviewApplicationRepository")
public class ReviewApplicationRepositoryImpl extends BaseRepositoryImpl<ReviewApplication, Long>
        implements ReviewApplicationRepository {
    public ReviewApplicationRepositoryImpl() {
        super(ReviewApplication.class);
    }
}
