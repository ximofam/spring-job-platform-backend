package com.htweb.admin.services;

import com.htweb.admin.repositories.PermissionRepository;
import com.htweb.core.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface PermissionService {
    Map<String, List<Permission>> findAllGroupedByModule();
    List<Permission> findAll();
    List<Permission> findAllById(List<Long> ids);
}
