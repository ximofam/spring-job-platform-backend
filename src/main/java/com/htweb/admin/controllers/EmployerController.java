package com.htweb.admin.controllers;

import com.htweb.admin.services.CompanyService;
import com.htweb.admin.services.EmployerProfileService;
import com.htweb.admin.services.RoleService;
import com.htweb.admin.services.UserService;
import com.htweb.admin.wrappers.EmployerUpdateForm;
import com.htweb.core.enums.*;
import com.htweb.core.pojo.Company;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users/employers")
@RequiredArgsConstructor
public class EmployerController {
    private final UserService userService;
    private final EmployerProfileService employerProfileService;
    private final CompanyService companyService;
    private final RoleService roleService;



    @GetMapping("")
    public String listView(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/pages/employer";
    }

    @ModelAttribute
    public void commonResponses(Model model) {
        model.addAttribute("genders", Gender.values());
        model.addAttribute("companyTypes", CompanyType.values());
        model.addAttribute("employeeSizes", EmployeeSize.values());
        model.addAttribute("companyStatuses", CompanyStatus.values());
        model.addAttribute("employerStatuses", EmployerStatus.values());
        model.addAttribute("userRoles", UserRole.values());
        model.addAttribute("allRoles", roleService.findAll());
    }
    @GetMapping("/{userId}/edit")
    public String editView(@PathVariable Long userId, Model model) {
        User user = userService.findByIdWithProfileAndCompany(userId).orElseThrow();

        EmployerUpdateForm form = new EmployerUpdateForm();
        form.setUser(user);
        form.setProfile(user.getEmployerProfile());
        form.setCompany(user.getEmployerProfile().getCompany());
        form.setDateOfBirth(user.getDateOfBirth());
        form.setRoles(user.getRoles());
        model.addAttribute("form", form);
        return "admin/pages/employer_edit";
    }

    @PostMapping("/{userId}/edit")
    public String handleUpdate(@PathVariable Long userId,
                               @ModelAttribute("form") EmployerUpdateForm form) {

        User user = userService.findById(userId).orElseThrow();
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
                "deletedAt", "createdAt", "updatedAt",
                "dateOfBirth");

        if (form.getDateOfBirth() != null) {
            user.setDateOfBirth(form.getDateOfBirth());
        }
        user.setRoles(form.getRoles());
        userService.update(user);

        EmployerProfile profile = employerProfileService.findById(userId).orElseThrow();
        BeanUtils.copyProperties(form.getProfile(), profile,
                "userId", "user", "company", "approvedBy", "approvedAt", "createdAt", "updatedAt");
        employerProfileService.update(profile);

        Company company = profile.getCompany();
        BeanUtils.copyProperties(form.getCompany(), company,
                "id", "address", "country", "deletedAt", "createdAt", "updatedAt");
        companyService.update(company);

        return "redirect:/admin/users/employers/" + userId + "/edit";
    }

    @PostMapping("/{userId}/approve")
    public String approve(@PathVariable Long userId) {
        EmployerProfile profile = employerProfileService.findById(userId).orElseThrow();
        profile.setStatus(EmployerStatus.APPROVED);
        employerProfileService.update(profile);

        Company company = profile.getCompany();
        company.setStatus(CompanyStatus.APPROVED);
        companyService.update(company);

        User user = userService.findByIdWithRoles(userId).orElseThrow();
        Role employerRole = roleService.findByName("EMPLOYER").orElseThrow();
        user.getRoles().add(employerRole);
        userService.update(user);

        return "redirect:/admin/users/employers";
    }

    @PostMapping("/{userId}/reject")
    public String reject(@PathVariable Long userId) {
        EmployerProfile profile = employerProfileService.findById(userId).orElseThrow();
        profile.setStatus(EmployerStatus.DENIED);
        employerProfileService.update(profile);

        Company company = profile.getCompany();
        company.setStatus(CompanyStatus.DENIED);
        companyService.update(company);

        User user = userService.findByIdWithRoles(userId).orElseThrow();
        Role employerRole = roleService.findByName("EMPLOYER").orElseThrow();
        user.getRoles().remove(employerRole);
        userService.update(user);

        return "redirect:/admin/users/employers";
    }
}
