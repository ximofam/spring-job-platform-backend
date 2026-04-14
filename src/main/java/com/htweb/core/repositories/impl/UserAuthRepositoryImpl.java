package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.Permission;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserAuthRepositoryImpl implements UserAuthRepository {
    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }

    @Override
    public List<String> getAuthoritiesByUsername(String username) {
        Session session = this.sessionFactory.getCurrentSession();

        String hql = "SELECT DISTINCT u FROM User u " +
                "LEFT JOIN FETCH u.roles r " +
                "LEFT JOIN FETCH r.permissions p " +
                "WHERE u.username = :username " +
                "AND u.isActive = true " +
                "AND (r.id IS NULL OR r.isActive = true) " +
                "AND (p.id IS NULL OR p.isActive = true)";

        User user = session.createQuery(hql, User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();

        if (user == null) {
            return List.of();
        }

        List<String> authorities = new ArrayList<>();

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                authorities.add(String.format("ROLE_%s", role.getName().toUpperCase()));

                if (role.getPermissions() != null) {
                    for (Permission p : role.getPermissions()) {
                        authorities.add(p.getName());
                    }
                }
            }
        }

        return authorities;
    }
}
