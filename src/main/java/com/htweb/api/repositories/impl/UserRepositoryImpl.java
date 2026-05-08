package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.UserRepository;
import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("apiUserRepository")
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {
    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session = this.getCurrentSession();
        String hql = """
                FROM User u LEFT JOIN FETCH u.country WHERE u.id = :id
                """;

        return session.createQuery(hql, User.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<User> findByUsername(String username) {
        Session session = this.getCurrentSession();
        String hql = """
                FROM User u LEFT JOIN FETCH u.country WHERE u.username = :username
                """;

        return session.createQuery(hql, User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public boolean isExistsUsername(String username) {
        return isFieldExists("username", username);
    }

    @Override
    public boolean isExistsEmail(String email) {
        return isFieldExists("email", email);
    }

    @Override
    @Transactional
    public void createCandidateProfile(CandidateProfile profile) {
        getCurrentSession().persist(profile);
    }

    @Override
    @Transactional
    public void createEmployerProfile(EmployerProfile profile) {
        getCurrentSession().persist(profile);
    }
}
