package com.htweb.admin.services.Impl;

import com.htweb.admin.repositories.PermissionRepository;
import com.htweb.admin.services.PermissionService;
import com.htweb.core.pojo.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final @Qualifier("adminPermissionRepositoryImpl") PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<Permission>> findAllGroupedByModule() {
        return this.permissionRepository.findAll()
                .stream().collect(Collectors.groupingBy(
                        Permission::getModule,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    @Override
    public List<Permission> findAll() {
        return this.permissionRepository.findAll();
    }

    @Override
    public List<Permission> findAllById(List<Long> ids) {
        return this.permissionRepository.findAllById(ids);
    }
}
