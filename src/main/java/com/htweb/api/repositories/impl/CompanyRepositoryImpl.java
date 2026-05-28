package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.CompanyRepository;
import com.htweb.core.pojo.Company;
import com.htweb.core.repositories.impl.PaginateRepositoryImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository("apiCompanyRepository")
public class CompanyRepositoryImpl extends PaginateRepositoryImpl<Company, Long> implements CompanyRepository {
    public CompanyRepositoryImpl() {
        super(Company.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Company> findBySlug(String slug) {
        String hql = "FROM Company c WHERE c.slug = :slug";
        return this.getCurrentSession().createQuery(hql, Company.class)
                .setParameter("slug", slug)
                .uniqueResultOptional();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<String> findSlugsByPrefix(String prefix, Long excludeId) {
        Session session = getCurrentSession();
        String hql = excludeId != null
                ? "SELECT slug FROM Company WHERE slug = :base OR slug LIKE :pattern AND id != :excludeId"
                : "SELECT slug FROM Company WHERE slug = :base OR slug LIKE :pattern";

        var query = session.createQuery(hql, String.class)
                .setParameter("base", prefix)
                .setParameter("pattern", prefix + "-%");

        if (excludeId != null) {
            query.setParameter("excludeId", excludeId);
        }

        return query.getResultList();
    }

    @Override
    public boolean isExistsTaxCode(String taxCode) {
        return isFieldExists("taxCode", taxCode);
    }

    @Override
    public boolean isExistsCompanyName(String name) {
        return isFieldExists("name", name);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Company> getByUserId(Long userId) {
        String hql = """
                SELECT c
                FROM EmployerProfile ep
                JOIN ep.company c
                WHERE ep.id = :userId
                """;

        return getCurrentSession().createQuery(hql, Company.class)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst();
    }
}
