package com.htweb.api.mappers;

import com.htweb.api.dtos.locations.AddressCreateRequest;
import com.htweb.api.dtos.locations.CitySimpleResponse;
import com.htweb.api.dtos.locations.DistrictSimpleResponse;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.repositories.CityRepository;
import com.htweb.api.repositories.DistrictRepository;
import com.htweb.core.pojo.Address;
import com.htweb.core.pojo.City;
import com.htweb.core.pojo.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LocationMapper {
    @Autowired
    @Qualifier("apiCityRepository")
    private CityRepository cityRepository;
    @Autowired
    @Qualifier("apiDistrictRepository")
    private DistrictRepository districtRepository;

    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "districtId", target = "district")
    public abstract Address toAddress(AddressCreateRequest request);

    public abstract List<CitySimpleResponse> toCitySimpleResponseList(List<City> cities);

    public abstract List<DistrictSimpleResponse> toDistrictSimpleResponseList(List<District> districts);

    protected City mapCity(Long id) {
        if (id == null) return null;

        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tỉnh/Thành này không tồn tại"));
    }

    protected District mapDistrict(Long id) {
        if (id == null) return null;

        return districtRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quận/Huyện này không tồn tại"));
    }
}
