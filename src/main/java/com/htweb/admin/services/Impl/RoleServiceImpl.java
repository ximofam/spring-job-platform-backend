package com.htweb.admin.services.Impl;


import com.htweb.admin.repositories.PermissionRepository;
import com.htweb.admin.repositories.RoleRepository;
import com.htweb.admin.services.RoleService;
import com.htweb.core.pojo.Permission;
import com.htweb.core.pojo.Role;
import com.htweb.core.services.impl.AuthorityCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepository;
    private final AuthorityCacheService authorityCacheService;

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }

    @Override
    public Role findById(long id) {
        return this.roleRepo.findById(id).orElseThrow();
    }

    @Override
    public Role update(Role role) {
        return this.roleRepo.update(role);
    }

    @Override
    public void save(Role role) {
        this.roleRepo.save(role);
    }

    @Override
    public void delete(Long id) {
        this.roleRepo.delete(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Set<Long> getPermissionIds(Long roleId) {
        Role role = this.findById(roleId);
        return role.getPermissions().stream().map(Permission::getId).collect(Collectors.toSet());
    }

    @Override
    public void updatePermissions(Long roleId, List<Long> permissionIds) {
        Role role = this.roleRepo.findById(roleId).orElseThrow();

        Set<Permission> newPerms = (permissionIds != null)
                ? new HashSet<>(this.permissionRepository.findAllById(permissionIds))
                : new HashSet<>();
        role.setPermissions(newPerms);
        roleRepo.save(role);
        authorityCacheService.evictCache(role.getName());
    }

    @Override
    public Optional<Role> findByName(String name) {
        return this.roleRepo.findByName(name);
    }
}
