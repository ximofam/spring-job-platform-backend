package com.htweb.api.dtos.company;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanySimpleResponse {
    private String slug;
    private String name;
    private String logoUrl;
}
