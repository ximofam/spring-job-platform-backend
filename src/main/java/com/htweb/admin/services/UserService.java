package com.htweb.admin.services;

import com.htweb.core.pojo.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findByIdWithProfileAndCompany(Long userId);
    User update(User user);
    Optional<User> findById(Long userId);
     Optional<User> findByIdWithRoles(Long userId);
}
