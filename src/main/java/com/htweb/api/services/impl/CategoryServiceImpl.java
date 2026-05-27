package com.htweb.api.services.impl;

import com.htweb.api.dtos.job.JobCategoryResponse;
import com.htweb.api.mappers.JobMapper;
import com.htweb.api.repositories.CategoryRepository;
import com.htweb.api.services.CategoryService;
import com.htweb.core.pojo.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("apiCategoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Qualifier("apiCategoryRepository")
    private final CategoryRepository categoryRepository;
    private final JobMapper jobMapper;

    @Override
    @Cacheable(value = "categories")
    @Transactional(readOnly = true)
    public List<JobCategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    JobCategoryResponse response = jobMapper.toJobCategoryResponse(category);

                    List<JobCategoryResponse> children = category.getChildren()
                            .stream()
                            .map(jobMapper::toJobCategoryResponse)
                            .collect(Collectors.toList());

                    response.setChildren(children);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
