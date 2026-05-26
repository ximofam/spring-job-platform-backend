package com.htweb.admin.services.Impl;

import com.htweb.admin.repositories.UserRepository;
import com.htweb.admin.services.UserService;
import com.htweb.core.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByIdWithProfileAndCompany(Long userId) {
        return userRepository.findByIdWithProfileAndCompany(userId);
    }

    @Override
    public User update(User user) {
        return this.userRepository.update(user);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByIdWithRoles(Long userId) {
        return userRepository.findByIdWithRoles(userId);
    }
//    private final EmployerProfileRepository employerProfileRepository;
//    private final CompanyRepository companyRepository;
}
