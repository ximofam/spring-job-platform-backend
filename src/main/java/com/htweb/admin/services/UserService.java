package com.htweb.admin.services;

import com.htweb.admin.wrappers.EmployerUpdateForm;
import com.htweb.core.pojo.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    User update(User user);
    User findById(Long userId);
    User findByIdWithRoles(Long userId);

}
