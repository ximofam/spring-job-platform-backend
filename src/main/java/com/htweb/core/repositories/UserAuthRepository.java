package com.htweb.core.repositories;

import com.htweb.core.pojo.User;

import java.util.Optional;

public interface UserAuthRepository {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
