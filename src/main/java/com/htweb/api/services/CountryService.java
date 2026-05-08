package com.htweb.api.services;

import com.htweb.api.dtos.country.CountryDetailResponse;

import java.util.List;

public interface CountryService {
    List<CountryDetailResponse> getAllCountries();
}
