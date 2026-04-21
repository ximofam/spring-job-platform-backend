package com.htweb.admin.services.Impl;

import com.htweb.admin.repositories.RoleRepository;
import com.htweb.admin.services.RoleService;
import com.htweb.core.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepo;

    @Override
    public List<Role> findAll() {
        return this.roleRepo.findAll();
    }
}
