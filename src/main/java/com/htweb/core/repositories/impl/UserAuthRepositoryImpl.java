package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserAuthRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserAuthRepository {
    public UserAuthRepositoryImpl() {
        super(User.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
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
