package com.htweb.admin.repositories.Impl;

import com.htweb.admin.repositories.UserRepository;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl
        extends BaseRepositoryImpl<User, Long>
        implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

//    @Override
//    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
//    public List<User> findAll() {
//        Session session = this.getCurrentSession();
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<User> query = cb.createQuery(User.class);
//        Root<User> root = query.from(User.class);
//
//
//
//        query.select(root);
//
//        return session.createQuery(query).getResultList();
//    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<User> findByIdWithRoles(Long userId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        root.fetch("roles", JoinType.LEFT);

        query.select(root)
                .where(cb.equal(root.get("id"), userId));

        return Optional.ofNullable(
                session.createQuery(query).uniqueResult()
        );
    }
}
