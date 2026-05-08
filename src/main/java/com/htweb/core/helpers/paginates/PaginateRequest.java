package com.htweb.core.helpers.paginates;

import com.htweb.core.helpers.queries.CriteriaFilter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginateRequest<T> {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private CriteriaFilter<T> filter;
    private String orderBy;
    private boolean orderDesc;
}