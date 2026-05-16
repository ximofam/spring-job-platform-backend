package com.htweb.api.mappers;

import com.htweb.api.dtos.country.CountryDetailResponse;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.repositories.CountryRepository;
import com.htweb.core.pojo.Country;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CountryMapper {
    @Autowired
    @Qualifier("apiCountryRepository")
    private CountryRepository countryRepository;

    public abstract List<CountryDetailResponse> toCountryDetailResList(List<Country> countries);

    public abstract CountryDetailResponse toCountryDetailResponse(Country country);

    public Country idToCountry(Long id) {
        if (id == null) return null;
        return countryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found country with id: %d", id));
    }
}
