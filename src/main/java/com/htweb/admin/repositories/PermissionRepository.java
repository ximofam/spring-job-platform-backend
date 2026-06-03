package com.htweb.admin.repositories;

import com.htweb.core.pojo.Permission;
import com.htweb.core.repositories.BaseRepository;

import java.util.List;

public interface PermissionRepository extends BaseRepository<Permission,Long> {
    List<Permission> findAllById(List<Long> ids);
}
