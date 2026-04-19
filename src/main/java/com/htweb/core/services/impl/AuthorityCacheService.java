package com.htweb.core.services.impl;

import com.htweb.core.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthorityCacheService {
    private final PermissionRepository permissionRepository;

    @Cacheable(value = "rolePermissionCache", key = "#roleName")
    public Set<GrantedAuthority> getCachedAuthorities(String roleName) {
        return permissionRepository.findByRoleName(roleName).stream()
                .map(per -> new SimpleGrantedAuthority(per.getName()))
                .collect(Collectors.toSet());
    }
}