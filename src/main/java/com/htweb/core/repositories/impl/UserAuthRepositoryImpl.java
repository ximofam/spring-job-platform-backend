package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserAuthRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserAuthRepository {
    public UserAuthRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Session session = this.getCurrentSession();
        User user = session.createQuery("FROM User u JOIN FETCH u.roles WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Session session = this.getCurrentSession();
        User user = session.createQuery("FROM User u JOIN FETCH u.roles WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findUserByUsernameOrEmail(String usernameOrEmail) {
        Session session = this.getCurrentSession();
        User user = session.createQuery(
                        "FROM User u JOIN FETCH u.roles WHERE (u.username = :usernameOrEmail or u.email = :usernameOrEmail)",
                        User.class)
                .setParameter("usernameOrEmail", usernameOrEmail)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }
}
