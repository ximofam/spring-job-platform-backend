package com.htweb.services;

import com.htweb.dtos.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto.DetailResponse getById(Integer id);

    List<CategoryDto.ItemResponse> findAll();
}
