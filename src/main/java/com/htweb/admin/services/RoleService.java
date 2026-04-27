package com.htweb.admin.services;
import com.htweb.core.pojo.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> findAll();
    Role findById(long id);
    Role update(Role role);
    Set<Long> getPermissionIds(Long roleId);
    Role save(Role role);
    void delete(Long id);
    void updatePermissions(Long roleId, List<Long> permissionIds);
}
