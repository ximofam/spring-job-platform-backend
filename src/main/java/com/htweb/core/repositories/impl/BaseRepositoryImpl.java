package com.htweb.core.repositories.impl;

import com.htweb.core.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseRepositoryImpl<T, ID extends Serializable> implements BaseRepository<T, ID> {
    @Autowired
    protected SessionFactory factory;

    protected final Class<T> entityClass;

    public BaseRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public Optional<T> findById(ID id) {
        Session s = this.getSession();

        return Optional.ofNullable(
                s.get(this.entityClass, id)
        );
    }

    @Override
    public List<T> findAll() {
        Session s = this.getSession();

        return s.createQuery(String.format("FROM %s", this.entityClass.getName()), this.entityClass)
                .getResultList();
    }

    @Override
    public T save(T entity) {
        this.getSession().persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return this.getSession().merge(entity);
    }

    @Override
    public void delete(ID id) {
        T entity = this.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));

        this.getSession().remove(entity);
    }

    @Override
    public void delete(T entity) {
        this.getSession().remove(entity);
    }

}
