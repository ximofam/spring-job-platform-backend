package com.htweb.core.repositories.impl;

import com.htweb.core.helpers.paginates.PaginateRequest;
import com.htweb.core.helpers.paginates.PaginateResponse;
import com.htweb.core.helpers.queries.CriteriaFilter;
import com.htweb.core.repositories.PaginateRepository;
import jakarta.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public class PaginateRepositoryImpl<T, ID extends Serializable>
        extends BaseRepositoryImpl<T, ID>
        implements PaginateRepository<T, ID> {

    public PaginateRepositoryImpl(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateResponse<T> paginate(PaginateRequest<T> request) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<T> dataQuery = cb.createQuery(entityClass);
        Root<T> root = dataQuery.from(entityClass);

        applyFilter(request.getFilter(), root, dataQuery, cb);
        applyOrder(request, root, dataQuery, cb);

        List<T> data = getCurrentSession()
                .createQuery(dataQuery)
                .setFirstResult((request.getPage() - 1) * request.getSize())
                .setMaxResults(request.getSize())
                .getResultList();
        
        long total = countQuery(request.getFilter(), cb);

        return PaginateResponse.<T>builder()
                .data(data)
                .page(request.getPage())
                .size(request.getSize())
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / request.getSize()))
                .build();
    }

    private void applyFilter(CriteriaFilter<T> filter, Root<T> root,
                             CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (filter != null) {
            Predicate predicate = filter.toPredicate(root, query, cb);
            if (predicate != null) {
                query.where(predicate);
            }
        }
    }

    private void applyOrder(PaginateRequest<T> request, Root<T> root,
                            CriteriaQuery<T> query, CriteriaBuilder cb) {
        if (request.getOrderBy() != null && !request.getOrderBy().isBlank()) {
            Path<?> orderPath = resolvePath(root, request.getOrderBy());
            query.orderBy(request.isOrderDesc()
                    ? cb.desc(orderPath)
                    : cb.asc(orderPath));
        }
    }

    private long countQuery(CriteriaFilter<T> filter, CriteriaBuilder cb) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        countQuery.select(cb.countDistinct(countRoot));

        applyFilter(filter, countRoot, countQuery, cb);

        return getCurrentSession()
                .createQuery(countQuery)
                .uniqueResult();
    }

    private Path<?> resolvePath(Root<T> root, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        Path<?> path = root;
        for (String part : parts) {
            path = path.get(part);
        }
        return path;
    }
}
