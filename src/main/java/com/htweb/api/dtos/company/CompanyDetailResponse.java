package com.htweb.api.dtos.company;

import com.htweb.api.dtos.country.CountryDetailResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDetailResponse {
    private Long id;
    private String name;
    private String taxCode;
    private String slug;
    private String logoUrl;
    private String employeeSize;
    private String type;
    private String description;
    private CountryDetailResponse country;
    private String address;
}