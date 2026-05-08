package com.htweb.api.services.impl;

import com.htweb.api.dtos.country.CountryDetailResponse;
import com.htweb.api.mappers.CountryMapper;
import com.htweb.api.repositories.CountryRepository;
import com.htweb.api.services.CountryService;
import com.htweb.core.pojo.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("apiCountryService")
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    @Qualifier("apiCountryRepository")
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryDetailResponse> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        if (countries != null && !countries.isEmpty()) {
            return countryMapper.toCountryDetailResList(countries);
        }

        return List.of();
    }
}
