package com.htweb.api.mappers;

import com.htweb.api.dtos.job.*;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.repositories.CategoryRepository;
import com.htweb.core.pojo.Category;
import com.htweb.core.pojo.Job;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, CompanyMapper.class})
public abstract class JobMapper {
    @Autowired
    @Qualifier("apiCategoryRepository")
    private CategoryRepository categoryRepository;

    @Mapping(target = "address", source = "address.fullAddress")
    public abstract JobSimpleResponse toJobSimpleResponse(Job job);

    public abstract List<JobSimpleResponse> toJobSimpleResponseList(List<Job> jobs);

    @Mapping(target = "address", source = "address.fullAddress")
    public abstract JobDetailResponse toJobDetailResponse(Job job);

    @Mapping(target = "children", ignore = true)
    public abstract JobCategoryResponse toJobCategoryResponse(Category category);

    @Mapping(source = "categoryId", target = "category")
    public abstract Job toJob(JobCreateRequest request);

    public abstract List<MyJobResponse> toMyJobResponseList(List<Job> jobs);

    public abstract MyJobDetailResponse toMyJobDetailResponse(Job job);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateJob(JobUpdateRequest request, @MappingTarget Job job);

    protected Category mapCategory(Long id) {
        if (id == null) return null;

        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ngành nghề này"));
    }
}
