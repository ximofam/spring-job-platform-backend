package com.htweb.api.dtos.company;

import com.htweb.api.dtos.country.CountryDetailResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDetailResponse {
    private Long id;
    private String name;
    private String slug;
    private String logoUrl;
    private String address;
    private String employeeSize;
    private String type;
    private String description;
    private CountryDetailResponse country;
}