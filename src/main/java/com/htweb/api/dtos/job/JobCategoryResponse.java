package com.htweb.api.dtos.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobCategoryResponse {
    private Long id;
    private String name;
    private List<JobCategoryResponse> children;
}
