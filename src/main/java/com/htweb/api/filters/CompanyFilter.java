package com.htweb.api.filters;

import com.htweb.core.enums.CompanyType;
import com.htweb.core.helpers.queries.CriteriaFilter;
import com.htweb.core.pojo.Company;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class CompanyFilter {
    public static CriteriaFilter<Company> search(String name, CompanyType type, Long countryId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getResultType() != Long.class) {
                root.fetch("country", JoinType.LEFT);
            }

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"
                ));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (countryId != null) {
                predicates.add(cb.equal(root.get("country").get("id"), countryId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
