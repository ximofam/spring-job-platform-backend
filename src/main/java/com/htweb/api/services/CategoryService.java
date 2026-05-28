package com.htweb.api.services;

import com.htweb.api.dtos.job.JobCategoryResponse;

import java.util.List;

public interface CategoryService {
    List<JobCategoryResponse> getAllCategories();
}
