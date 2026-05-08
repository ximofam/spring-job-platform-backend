package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.CountryRepository;
import com.htweb.core.pojo.Country;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("apiCountryRepository")
public class CountryRepositoryImpl extends BaseRepositoryImpl<Country, Long> implements CountryRepository {
    public CountryRepositoryImpl() {
        super(Country.class);
    }

    @Override
    public List<Country> findAll() {
        Session session = this.getCurrentSession();

        return session.createQuery("FROM Country c ORDER BY c.code ASC", Country.class)
                .getResultList();
    }
}
