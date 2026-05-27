package com.htweb.api.repositories;

import com.htweb.core.pojo.Company;
import com.htweb.core.repositories.PaginateRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends PaginateRepository<Company, Long> {
    Optional<Company> findBySlug(String slug);

    List<String> findSlugsByPrefix(String prefix, Long excludeId);

    boolean isExistsTaxCode(String taxCode);

    boolean isExistsCompanyName(String name);

    Optional<Company> getByUserId(Long id);
}
