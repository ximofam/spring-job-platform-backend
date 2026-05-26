package com.htweb.admin.repositories;

import com.htweb.core.pojo.User;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
    @Override
    List<User> findAll();

    Optional<User> findByIdWithProfileAndCompany(Long userId);

    Optional<User> findByIdWithRoles(Long userId);
}
