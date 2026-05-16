package com.htweb.api.dtos.country;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDetailResponse {
    private Long id;
    private String code;
    private String name;
    private String imageUrl;
}
