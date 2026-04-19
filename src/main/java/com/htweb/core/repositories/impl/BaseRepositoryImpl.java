package com.htweb.core.repositories.impl;

import com.htweb.core.pojo.BaseModel;
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

    protected boolean isSoftDeletable() {
        return BaseModel.class.isAssignableFrom(entityClass);
    }

    protected Session getCurrentSession() {
        Session session = this.factory.getCurrentSession();
        if (isSoftDeletable() && session.getEnabledFilter("activeFilter") == null) {
            session.enableFilter("activeFilter")
                    .setParameter("isActive", true);
        }

        return session;
    }

    protected Session getSessionWithoutActiveFilter() {
        Session session = factory.getCurrentSession();
        session.disableFilter("activeFilter");
        return session;
    }

    @Override
    public Optional<T> findById(ID id) {
        String hql = String.format("FROM %s e WHERE e.id = :id", entityClass.getName());

        return Optional.ofNullable(
                getCurrentSession()
                        .createQuery(hql, entityClass)
                        .setParameter("id", id)
                        .uniqueResult()
        );
    }

    @Override
    public List<T> findAll() {
        Session s = this.getCurrentSession();

        return s.createQuery(String.format("FROM %s", this.entityClass.getName()), this.entityClass)
                .getResultList();
    }

    @Override
    public T save(T entity) {
        this.getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return this.getCurrentSession().merge(entity);
    }

    @Override
    public void delete(ID id) {
        if (isSoftDeletable()) {
            softDelete(id);
        } else {
            hardDelete(id);
        }
    }

    public void softDelete(ID id) {
        if (!isSoftDeletable()) {
            throw new UnsupportedOperationException(
                    entityClass.getSimpleName() + " does not support soft delete"
            );
        }

        String hql = String.format("UPDATE %s e SET e.isActive = false WHERE e.id = :id", this.entityClass.getName());

        int affectRow = getCurrentSession()
                .createMutationQuery(hql)
                .setParameter("id", id)
                .executeUpdate();

        if (affectRow == 0) {
            throw new RuntimeException("Entity not found");
        }
    }

    public void hardDelete(ID id) {
        String hql = String.format("DELETE FROM %s e WHERE e.id = :id", entityClass.getName());

        int affectRow = getSessionWithoutActiveFilter()
                .createMutationQuery(hql)
                .setParameter("id", id)
                .executeUpdate();

        if (affectRow == 0) {
            throw new RuntimeException("Entity not found");
        }
    }
}
