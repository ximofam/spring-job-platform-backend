package com.htweb.core.repositories;

import com.htweb.core.helpers.paginates.PaginateRequest;
import com.htweb.core.helpers.paginates.PaginateResponse;

import java.io.Serializable;

public interface PaginateRepository<T, ID extends Serializable> extends BaseRepository<T, ID> {
    PaginateResponse<T> paginate(PaginateRequest<T> request);
}
