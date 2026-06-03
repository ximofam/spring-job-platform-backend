package com.htweb.api.dtos.job;

import com.htweb.api.dtos.locations.AddressCreateRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JobUpdateRequest {
    private String title;
    private String description;
    private String requirements;
    private String benefit;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryCurrency;
    private String employmentType;
    private String experienceLevel;
    private AddressCreateRequest address;
}
