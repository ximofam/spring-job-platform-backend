package com.htweb.api.services;


import com.htweb.api.dtos.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto.DetailResponse getById(Integer id);

    List<CategoryDto.ItemResponse> findAll();
}
