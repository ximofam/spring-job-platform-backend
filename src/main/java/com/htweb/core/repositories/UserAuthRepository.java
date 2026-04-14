package com.htweb.core.repositories;

import com.htweb.core.pojo.User;

import java.util.List;
import java.util.Optional;

public interface UserAuthRepository {
    Optional<User> findUserByUsername(String username);

    List<String> getAuthoritiesByUsername(String username);
}
