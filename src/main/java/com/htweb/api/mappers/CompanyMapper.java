package com.htweb.api.mappers;

import com.htweb.api.dtos.auth.AuthRegisterEmployerRequest;
import com.htweb.api.dtos.company.CompanyDetailResponse;
import com.htweb.api.dtos.company.CompanySimpleResponse;
import com.htweb.core.pojo.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface CompanyMapper {
    @Mapping(source = "companyName", target = "name")
    @Mapping(source = "companyType", target = "type")
    @Mapping(source = "companyEmployeeSize", target = "employeeSize")
    @Mapping(source = "companyDescription", target = "description")
    @Mapping(source = "companyTaxCode", target = "taxCode")
    @Mapping(source = "companyCountryId", target = "country")
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    Company toCompany(AuthRegisterEmployerRequest request);

    CompanyDetailResponse toCompanyDetailResponse(Company company);

    List<CompanySimpleResponse> toCompanySimpleResponseList(List<Company> companies);
}
