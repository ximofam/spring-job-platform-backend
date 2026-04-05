package com.htweb.admin.repositories.impl;

import com.htweb.admin.repositories.CategoryRepository;
import com.htweb.core.pojo.Category;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("adminCategoryRepository")
public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category, Integer> implements CategoryRepository {
    public CategoryRepositoryImpl() {
        super(Category.class);
    }
}
