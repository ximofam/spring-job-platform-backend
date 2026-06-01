package com.htweb.api.dtos.application;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApplicationCreateRequest {
    private Long jobId;
    private Long cvId;
    private String note;
    private BigDecimal expectedSalary;
}
