package com.htweb.api.dtos.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htweb.api.dtos.company.CompanyDetailResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class JobDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String benefit;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryCurrency;
    private String employmentType;
    private String experienceLevel;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant publishedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant expiredAt;
    private String address;
    private Integer boostScore = 0;
    private CompanyDetailResponse company;
}
