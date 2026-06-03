package com.htweb.admin.services.Impl;

import com.htweb.admin.services.*;
import com.htweb.admin.utils.SecurityHelper;
import com.htweb.admin.wrappers.EmployerUpdateForm;
import com.htweb.core.enums.CompanyStatus;
import com.htweb.core.enums.EmployerStatus;
import com.htweb.core.pojo.Company;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {
    private final RoleService roleService;
    private final UserService userService;
    @Qualifier("adminEmployerProfileServiceImpl")
    private final EmployerProfileService employerProfileService;
    private final CompanyService companyService;
    private final SecurityHelper securityHelper;

    @Transactional
    @Override
    public void updateEmployer(Long userId, EmployerUpdateForm form) {
        // Update User
        User user = userService.findById(userId);
        User formUser = form.getUser();

        if (formUser.getPhone() != null && formUser.getPhone().isBlank()) {
            formUser.setPhone(null);
        }
        if (formUser.getEmail() != null && formUser.getEmail().isBlank()) {
            formUser.setEmail(null);
        }
        if (formUser.getUsername() != null && formUser.getUsername().isBlank()) {
            formUser.setUsername(null);
        }

        BeanUtils.copyProperties(formUser, user,
                "id", "passwordHash", "refreshTokens",
                "deletedAt", "createdAt", "updatedAt", "dateOfBirth");

        if (form.getDateOfBirth() != null) {
            user.setDateOfBirth(form.getDateOfBirth());
        }
        user.setRoles(form.getRoles());
        userService.update(user);

        EmployerProfile profile = employerProfileService.findById(userId);
        BeanUtils.copyProperties(form.getProfile(), profile,
                "id", "user", "company", "approvedBy", "approvedAt", "createdAt", "updatedAt");
        employerProfileService.update(profile);

        Company company = profile.getCompany();
        BeanUtils.copyProperties(form.getCompany(), company,
                "id", "address", "country", "deletedAt", "createdAt", "updatedAt");
        companyService.update(company);
    }

    @Transactional
    @Override
    public void approveEmployer(Long userId) {
        User approver = securityHelper.getCurrentUser();

        EmployerProfile profile = employerProfileService.findById(userId);
        profile.setStatus(EmployerStatus.APPROVED);
        profile.setApprovedBy(approver);
        profile.setApprovedAt(Instant.now());
        employerProfileService.update(profile);

        Company company = profile.getCompany();
        company.setStatus(CompanyStatus.APPROVED);
        companyService.update(company);

        User user = userService.findByIdWithRoles(userId);
        Role employerRole = roleService.findByName("EMPLOYER").orElseThrow();
        user.getRoles().add(employerRole);
        userService.update(user);
    }

    @Transactional
    @Override
    public void rejectEmployer(Long userId) {
        EmployerProfile profile = employerProfileService.findById(userId);
        profile.setStatus(EmployerStatus.DENIED);
        employerProfileService.update(profile);

        Company company = profile.getCompany();
        company.setStatus(CompanyStatus.DENIED);
        companyService.update(company);

        User user = userService.findByIdWithRoles(userId);
        Role employerRole = roleService.findByName("EMPLOYER").orElseThrow();
        user.getRoles().remove(employerRole);
        userService.update(user);
    }

    @Override
    @Transactional
    public EmployerUpdateForm getEmployerEditForm(Long userId) {
        User user = userService.findByIdWithRoles(userId);

        EmployerUpdateForm form = new EmployerUpdateForm();
        EmployerProfile employerProfile = employerProfileService.findById(user.getId());
        form.setUser(user);
        form.setProfile(employerProfile);
        Company company = employerProfile.getCompany();
        System.out.println("=============================");
        System.out.println(company.getName());
        form.setCompany(company);
        form.setDateOfBirth(user.getDateOfBirth());
        form.setRoles(user.getRoles());
        return form;
    }
}
