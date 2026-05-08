package com.htweb.core.repositories;

import com.htweb.core.pojo.Permission;

import java.util.Set;

public interface PermissionRepository {
    Set<Permission> findByRoleName(String roleName);
}
