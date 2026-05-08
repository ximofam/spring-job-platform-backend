package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserAuthRepositoryImpl implements UserAuthRepository {
    @Autowired
    private SessionFactory factory;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<User> findUserByUsername(String username) {
        Session session = factory.getCurrentSession();
        return session.createQuery(
                        "FROM User u WHERE u.username = :username",
                        User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<User> findUserByEmail(String email) {
        Session session = factory.getCurrentSession();
        return session.createQuery(
                        "FROM User u WHERE u.email = :email",
                        User.class)
                .setParameter("email", email)
                .uniqueResultOptional();
    }
}
