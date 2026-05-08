package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.RoleRepository;
import com.htweb.core.pojo.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("apiRoleRepository")
public class RoleRepositoryImpl implements RoleRepository {
    @Autowired
    private SessionFactory factory;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Role> findByName(String name) {
        Session session = factory.getCurrentSession();

        return session.createQuery("FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }
}
