package com.htweb.api.dtos.job;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSearchRequest {
    private String q;
    private Long salaryMin;
    private Long salaryMax;
    private String employmentType;
    private String experienceLevel;
    private Long categoryId;
    private Long districtId;
    @Min(value = 1, message = "Page phải bắt đầu từ 1")
    private int page = 1;
    @Min(value = 2, message = "Số lượng tối thiểu cho 1 page là 2")
    @Max(value = 20, message = "Số lượng tối da cho 1 page là 20")
    private int size = 10;
}
