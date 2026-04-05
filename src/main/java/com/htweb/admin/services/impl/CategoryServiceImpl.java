package com.htweb.admin.services.impl;

import com.htweb.admin.repositories.CategoryRepository;
import com.htweb.admin.services.CategoryService;
import com.htweb.core.pojo.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("adminCategoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Qualifier("adminCategoryRepository")
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
