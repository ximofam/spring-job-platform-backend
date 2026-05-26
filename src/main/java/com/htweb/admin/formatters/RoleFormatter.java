package com.htweb.admin.formatters;


import com.htweb.admin.repositories.RoleRepository;
import com.htweb.core.pojo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RoleFormatter implements Formatter<Role> {

    private final RoleRepository roleRepository;

    @Override
    public Role parse(String id, Locale locale) {
        return roleRepository.findById(Long.parseLong(id)).orElseThrow();
    }

    @Override
    public String print(Role role, Locale locale) {
        return role.getId().toString();
    }
}
