package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAuthRepositoryImpl implements UserAuthRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("FROM User u JOIN FETCH u.roles WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }
}
