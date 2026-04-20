package com.htweb.api.services;

import com.htweb.api.dtos.UserDto;

public interface UserService {
    UserDto.DetailResponse getUserById(Long id);
}
