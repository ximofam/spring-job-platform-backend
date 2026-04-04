package com.htweb.mappers;


import com.htweb.dtos.CategoryDto;
import com.htweb.pojo.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto.DetailResponse toDetailResponse(Category category);

    List<CategoryDto.ItemResponse> toListResponse(List<Category> categories);

    Category toEntity(CategoryDto.CreateRequest dto);
}
