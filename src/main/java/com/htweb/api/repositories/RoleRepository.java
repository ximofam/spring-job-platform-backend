package com.htweb.api.repositories;

import com.htweb.core.pojo.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(String name);
}
