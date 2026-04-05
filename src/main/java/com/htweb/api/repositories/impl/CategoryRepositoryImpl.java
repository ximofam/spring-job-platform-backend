package com.htweb.api.repositories.impl;


import com.htweb.api.repositories.CategoryRepository;
import com.htweb.core.pojo.Category;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiCategoryRepository")
public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category, Integer> implements CategoryRepository {

    public CategoryRepositoryImpl() {
        super(Category.class);
    }
}
