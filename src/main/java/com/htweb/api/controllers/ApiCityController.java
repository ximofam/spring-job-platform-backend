package com.htweb.api.controllers;

import com.htweb.api.dtos.locations.CitySimpleResponse;
import com.htweb.api.dtos.locations.DistrictSimpleResponse;
import com.htweb.api.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class ApiCityController {
    @Qualifier("apiCityService")
    private final CityService cityService;

    @GetMapping
    public ResponseEntity<List<CitySimpleResponse>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/{id}/districts")
    public ResponseEntity<List<DistrictSimpleResponse>> getDistrictsOfCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getDistrictsOfCity(id));
    }
}
