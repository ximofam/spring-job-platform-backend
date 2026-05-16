package com.htweb.core.repositories.impl;

import com.htweb.core.helpers.models.SoftDeleteModel;
import com.htweb.core.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Instant;
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
        return SoftDeleteModel.class.isAssignableFrom(entityClass);
    }

    protected Session getCurrentSession() {
        return factory.getCurrentSession();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected boolean isFieldExists(String field, String value) {
        Session session = this.getCurrentSession();
        String hql = String.format(
                "SELECT count(e.id) FROM %s e WHERE e.%s = :value", entityClass.getName(), field
        );

        Long count = session.createQuery(hql, Long.class)
                .setParameter("value", value)
                .uniqueResult();

        return count != null && count > 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
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
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<T> findAll() {
        Session s = this.getCurrentSession();

        return s.createQuery(String.format("FROM %s", this.entityClass.getName()), this.entityClass)
                .getResultList();
    }

    @Override
    @Transactional
    public void save(T entity) {
        this.getCurrentSession().persist(entity);
    }

    @Override
    @Transactional
    public T update(T entity) {
        return this.getCurrentSession().merge(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (isSoftDeletable()) {
            softDelete(id);
        } else {
            hardDelete(id);
        }
    }

    @Override
    public T getReference(ID id) {
        return getCurrentSession().getReference(entityClass, id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void softDelete(ID id) {
        if (!isSoftDeletable()) {
            throw new UnsupportedOperationException(
                    entityClass.getSimpleName() + " does not support soft delete"
            );
        }

        String hql = String.format("UPDATE %s e SET e.deletedAt = :deletedAt WHERE e.id = :id",
                this.entityClass.getName());

        int affectRow = getCurrentSession()
                .createMutationQuery(hql)
                .setParameter("id", id)
                .setParameter("deletedAt", Instant.now())
                .executeUpdate();

        if (affectRow == 0) {
            throw new RuntimeException("Entity not found");
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void hardDelete(ID id) {
        String hql = String.format("DELETE FROM %s e WHERE e.id = :id", entityClass.getName());

        int affectRow = getCurrentSession()
                .createMutationQuery(hql)
                .setParameter("id", id)
                .executeUpdate();

        if (affectRow == 0) {
            throw new RuntimeException("Entity not found");
        }
    }
}
