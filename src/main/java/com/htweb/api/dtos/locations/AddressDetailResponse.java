package com.htweb.api.dtos.locations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDetailResponse {
    private CitySimpleResponse city;
    private DistrictSimpleResponse district;
    private String streetAddress;
}
