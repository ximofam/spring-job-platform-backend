package com.htweb.core.services.impl;

import com.htweb.core.pojo.Role;
import com.htweb.core.services.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityCacheService authorityCacheService;

    @Override
    public Set<GrantedAuthority> getAuthorities(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }

        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .toList();

        return getAuthorities(roleNames);
    }

    @Override
    public Set<GrantedAuthority> getAuthorities(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }

        Set<GrantedAuthority> res = new HashSet<>(
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role)))
                        .toList()
        );
        
        for (String role : roles) {
            res.addAll(authorityCacheService.getCachedAuthorities(role));
        }

        return res;
    }
}
