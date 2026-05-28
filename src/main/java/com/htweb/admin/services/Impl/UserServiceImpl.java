package com.htweb.admin.services.Impl;

import com.htweb.admin.repositories.UserRepository;
import com.htweb.admin.services.EmployerProfileService;
import com.htweb.admin.services.UserService;
import com.htweb.admin.wrappers.EmployerUpdateForm;
import com.htweb.core.pojo.Company;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Qualifier("adminEmployerProfileServiceImpl")
    private final EmployerProfileService employerProfileService;


    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        return this.userRepository.update(user);
    }

    @Override
    public User findById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow();
    }

    @Override
    public User findByIdWithRoles(Long userId) {
        return userRepository.findByIdWithRoles(userId).orElseThrow();
    }


//    private final EmployerProfileRepository employerProfileRepository;
//    private final CompanyRepository companyRepository;
}
