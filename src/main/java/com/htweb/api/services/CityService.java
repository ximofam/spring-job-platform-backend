package com.htweb.api.services;

import com.htweb.api.dtos.locations.CitySimpleResponse;
import com.htweb.api.dtos.locations.DistrictSimpleResponse;

import java.util.List;

public interface CityService {
    List<CitySimpleResponse> getAllCities();

    List<DistrictSimpleResponse> getDistrictsOfCity(Long id);
}
