package com.htweb.api.services.impl;

import com.htweb.api.dtos.CategoryDto;
import com.htweb.api.exceptions.base.NotFoundException;
import com.htweb.api.mappers.CategoryMapper;
import com.htweb.api.repositories.CategoryRepository;
import com.htweb.api.services.CategoryService;
import com.htweb.core.pojo.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service("apiCategoryService")
public class CategoryServiceImpl implements CategoryService {
    @Qualifier("apiCategoryRepository")
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public CategoryDto.DetailResponse getById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id %d do not exists", id));

        return categoryMapper.toDetailResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto.ItemResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();

        return categoryMapper.toListResponse(categories);
    }
}
