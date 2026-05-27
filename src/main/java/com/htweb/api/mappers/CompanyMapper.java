package com.htweb.api.mappers;

import com.htweb.api.dtos.company.CompanyCreateRequest;
import com.htweb.api.dtos.company.CompanyDetailResponse;
import com.htweb.api.dtos.company.CompanySimpleResponse;
import com.htweb.core.pojo.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CountryMapper.class, LocationMapper.class})
public interface CompanyMapper {
    @Mapping(source = "countryId", target = "country")
    Company toCompany(CompanyCreateRequest request);

    @Mapping(target = "address", source = "address.fullAddress")
    CompanyDetailResponse toCompanyDetailResponse(Company company);

    List<CompanySimpleResponse> toCompanySimpleResponseList(List<Company> companies);

    CompanySimpleResponse toCompanySimpleResponse(Company company);
}
