package com.htweb.api.services;

import com.htweb.api.dtos.company.CompanyDetailResponse;
import com.htweb.api.dtos.company.CompanySimpleResponse;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Company;

import java.util.Map;

public interface CompanyService {
    CompanyDetailResponse getCompanyBySlug(String slug);

    void create(Company company);

    boolean isExistsTaxCode(String taxCode);

    PaginateResponse<CompanySimpleResponse> search(Map<String, String> params);
}
