package com.htweb.api.mappers;


import com.htweb.api.dtos.CategoryDto;
import com.htweb.core.pojo.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto.DetailResponse toDetailResponse(Category category);

    List<CategoryDto.ItemResponse> toListResponse(List<Category> categories);

    Category toEntity(CategoryDto.CreateRequest dto);
}
