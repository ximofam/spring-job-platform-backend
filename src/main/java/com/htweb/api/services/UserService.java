package com.htweb.api.services;

import com.htweb.api.dtos.user.UserDetailResponse;

public interface UserService {
    UserDetailResponse getUserById(Long id);
}
