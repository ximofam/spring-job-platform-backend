package com.htweb.services.impl;

import com.htweb.dtos.CategoryDto;
import com.htweb.exceptions.base.NotFoundException;
import com.htweb.mappers.CategoryMapper;
import com.htweb.pojo.Category;
import com.htweb.repositories.CategoryRepository;
import com.htweb.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

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
