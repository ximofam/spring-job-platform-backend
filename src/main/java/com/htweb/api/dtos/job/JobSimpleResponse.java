package com.htweb.api.dtos.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htweb.api.dtos.company.CompanySimpleResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class JobSimpleResponse {
    private Long id;
    private String title;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryCurrency;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant publishedAt;
    private Integer boostScore = 0;
    private CompanySimpleResponse company;
}
