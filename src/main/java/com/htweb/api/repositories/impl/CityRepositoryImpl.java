package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.CityRepository;
import com.htweb.core.pojo.City;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiCityRepository")
public class CityRepositoryImpl extends BaseRepositoryImpl<City, Long> implements CityRepository {
    public CityRepositoryImpl() {
        super(City.class);
    }
}
