package com.htweb.admin.repositories;

import com.htweb.core.pojo.Role;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends BaseRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
