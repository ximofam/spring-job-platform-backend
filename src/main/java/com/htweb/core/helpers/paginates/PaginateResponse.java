package com.htweb.core.helpers.paginates;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
@Builder
public class PaginateResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public <R> PaginateResponse<R> map(Function<List<T>, List<R>> mapper) {
        return PaginateResponse.<R>builder()
                .data(mapper.apply(this.data))
                .page(this.page)
                .size(this.size)
                .totalElements(this.totalElements)
                .totalPages(this.totalPages)
                .build();
    }
}