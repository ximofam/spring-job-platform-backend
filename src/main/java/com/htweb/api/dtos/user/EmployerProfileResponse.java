package com.htweb.api.dtos.user;

import com.htweb.api.dtos.company.CompanyDetailResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class EmployerProfileResponse {
    private String status;
    private UserSimpleResponse approvedBy;
    private Instant approvedAt;
    private CompanyDetailResponse company;
}
