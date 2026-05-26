package com.htweb.api.services.impl;

import com.htweb.api.dtos.locations.CitySimpleResponse;
import com.htweb.api.dtos.locations.DistrictSimpleResponse;
import com.htweb.api.mappers.LocationMapper;
import com.htweb.api.repositories.CityRepository;
import com.htweb.api.services.CityService;
import com.htweb.core.pojo.City;
import com.htweb.core.pojo.District;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("apiCityService")
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    @Qualifier("apiCityRepository")
    private final CityRepository cityRepository;
    private final LocationMapper locationMapper;

    @Override
    @Cacheable(value = "cities")
    public List<CitySimpleResponse> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return locationMapper.toCitySimpleResponseList(cities);
    }

    @Override
    @Cacheable(value = "districts", key = "'city:' + #id")
    @Transactional(readOnly = true)
    public List<DistrictSimpleResponse> getDistrictsOfCity(Long id) {
        City city = cityRepository.findById(id).orElse(null);
        if (city == null) {
            return List.of();
        }

        List<District> districts = city.getDistricts().stream()
                .sorted(Comparator.comparing(District::getName))
                .collect(Collectors.toList());

        return locationMapper.toDistrictSimpleResponseList(districts);
    }
}
