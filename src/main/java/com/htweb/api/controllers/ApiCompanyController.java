package com.htweb.api.controllers;

import com.htweb.api.dtos.company.CompanyDetailResponse;
import com.htweb.api.dtos.company.CompanySimpleResponse;
import com.htweb.api.services.CompanyService;
import com.htweb.core.helpers.paginates.PaginateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class ApiCompanyController {
    @Qualifier("apiCompanyService")
    private final CompanyService companyService;

    @GetMapping("/{slug}")
    public ResponseEntity<CompanyDetailResponse> getCompanyBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(companyService.getCompanyBySlug(slug));
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<CompanySimpleResponse>> searchCompanies(
            @RequestParam Map<String, String> params) {

        return ResponseEntity.ok(companyService.search(params));
    }
}
