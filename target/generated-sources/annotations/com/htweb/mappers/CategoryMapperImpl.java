package com.htweb.mappers;

import com.htweb.dtos.CategoryDto;
import com.htweb.pojo.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T00:54:22+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto.DetailResponse toDetailResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto.DetailResponse detailResponse = new CategoryDto.DetailResponse();

        detailResponse.setId( category.getId() );
        detailResponse.setName( category.getName() );
        detailResponse.setDescription( category.getDescription() );

        return detailResponse;
    }

    @Override
    public List<CategoryDto.ItemResponse> toListResponse(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryDto.ItemResponse> list = new ArrayList<CategoryDto.ItemResponse>( categories.size() );
        for ( Category category : categories ) {
            list.add( categoryToItemResponse( category ) );
        }

        return list;
    }

    @Override
    public Category toEntity(CategoryDto.CreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Category category = new Category();

        category.setName( dto.getName() );
        category.setDescription( dto.getDescription() );

        return category;
    }

    protected CategoryDto.ItemResponse categoryToItemResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto.ItemResponse itemResponse = new CategoryDto.ItemResponse();

        itemResponse.setId( category.getId() );
        itemResponse.setName( category.getName() );

        return itemResponse;
    }
}
