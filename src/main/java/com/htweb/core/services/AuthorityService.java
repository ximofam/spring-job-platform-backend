package com.htweb.core.services;

import com.htweb.core.pojo.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface AuthorityService {
    Set<GrantedAuthority> getAuthorities(Set<Role> roles);

    Set<GrantedAuthority> getAuthorities(List<String> roles);
}
