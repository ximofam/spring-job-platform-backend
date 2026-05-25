package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.DistrictRepository;
import com.htweb.core.pojo.District;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository("apiDistrictRepository")
public class DistrictRepositoryImpl extends BaseRepositoryImpl<District, Long> implements DistrictRepository {
    public DistrictRepositoryImpl() {
        super(District.class);
    }
}
