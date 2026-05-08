package com.htweb.api.controllers;

import com.htweb.api.dtos.country.CountryDetailResponse;
import com.htweb.api.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class ApiCountryController {
    @Qualifier("apiCountryService")
    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDetailResponse>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }
}
