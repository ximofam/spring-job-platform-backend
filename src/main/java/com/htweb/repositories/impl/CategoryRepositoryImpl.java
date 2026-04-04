package com.htweb.repositories.impl;

import com.htweb.pojo.Category;
import com.htweb.repositories.CategoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category, Integer> implements CategoryRepository {

    public CategoryRepositoryImpl() {
        super(Category.class);
    }
}
