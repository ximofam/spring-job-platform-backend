package com.htweb.api.services.impl;

import com.htweb.api.dtos.company.CompanyDetailResponse;
import com.htweb.api.dtos.company.CompanySimpleResponse;
import com.htweb.api.exceptions.http.NotFoundException;
import com.htweb.api.filters.CompanyFilter;
import com.htweb.api.mappers.CompanyMapper;
import com.htweb.api.repositories.CompanyRepository;
import com.htweb.api.services.CompanyService;
import com.htweb.core.enums.CompanyType;
import com.htweb.core.helpers.paginates.PaginateRequest;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.pojo.Company;
import com.htweb.core.utils.SlugUtils;
import com.htweb.core.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("apiCompanyService")
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    @Qualifier("apiCompanyRepository")
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional(readOnly = true)
    public CompanyDetailResponse getCompanyBySlug(String slug) {
        Company company = companyRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Not found company with slug: %s", slug));

        return companyMapper.toCompanyDetailResponse(company);
    }

    @Override
    @Transactional
    public void create(Company company) {
        String baseSlug = SlugUtils.toSlug(company.getName());
        company.setSlug(generateUniqueSlug(baseSlug));
        companyRepository.save(company);
    }

    @Override
    public boolean isExistsTaxCode(String taxCode) {
        return companyRepository.isExistsTaxCode(taxCode);
    }

    @Override
    public PaginateResponse<CompanySimpleResponse> search(Map<String, String> params) {
        int page = Utils.parseInt(params.get("page"), 1);
        int size = Utils.parseInt(params.get("size"), 10);
        String name = params.getOrDefault("name", null);
        Long countryId = Utils.parseLong(params.get("countryId"));
        CompanyType type = params.get("type") != null ? CompanyType.valueOf(params.get("type")) : null;
        String orderBy = params.getOrDefault("orderBy", "createdAt");
        boolean orderDesc = Utils.parseBool(params.get("orderDesc"));

        PaginateRequest<Company> request = PaginateRequest.<Company>builder()
                .page(page)
                .size(size)
                .filter(CompanyFilter.search(name, type, countryId))
                .orderBy(orderBy)
                .orderDesc(orderDesc)
                .build();

        return companyRepository.paginate(request)
                .map(companyMapper::toCompanySimpleResponseList);
    }


    private String generateUniqueSlug(String baseSlug) {
        return generateUniqueSlug(baseSlug, null);
    }

    private String generateUniqueSlug(String baseSlug, Long excludeId) {
        List<String> existingSlugs = companyRepository.findSlugsByPrefix(baseSlug, excludeId);

        if (!existingSlugs.contains(baseSlug)) {
            return baseSlug;
        }

        int maxSuffix = existingSlugs.stream()
                .filter(s -> s.matches(baseSlug + "-\\d+"))
                .map(s -> Integer.parseInt(s.substring(baseSlug.length() + 1)))
                .max(Integer::compareTo)
                .orElse(0);

        return baseSlug + "-" + (maxSuffix + 1);
    }
}
