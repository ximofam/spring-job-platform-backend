package com.htweb.core.repositories;

import com.htweb.core.pojo.User;

import java.util.Optional;

public interface UserAuthRepository extends BaseRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
