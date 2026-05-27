package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.CategoryRepository;
import com.htweb.core.pojo.Category;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("apiCategoryRepository")
public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category, Long> implements CategoryRepository {
    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public List<Category> findAll() {
        Session session = getCurrentSession();

        return session.createQuery(
                        "SELECT c FROM Category c " +
                                "LEFT JOIN FETCH c.children " +
                                "WHERE c.parent IS NULL", Category.class)
                .getResultList();
    }
}
