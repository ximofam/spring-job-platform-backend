package com.htweb.admin.controllers;

import com.htweb.admin.services.*;
import com.htweb.admin.wrappers.EmployerUpdateForm;
import com.htweb.core.enums.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users/employers")
@RequiredArgsConstructor
public class EmployerController {
    @Qualifier("adminEmployerProfileServiceImpl")
    private final EmployerProfileService employerProfileService;
    private final RoleService roleService;
    private final EmployerService employerService;

    @GetMapping("")
    public String listView(Model model) {
        model.addAttribute("profiles", employerProfileService.findAll());
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
        EmployerUpdateForm form = employerService.getEmployerEditForm(userId);
        model.addAttribute("form", form);
        return "admin/pages/employer_edit";
    }

    @PostMapping("/{userId}/edit")
    public String handleUpdate(@PathVariable Long userId,
                               @ModelAttribute("form") EmployerUpdateForm form) {
        employerService.updateEmployer(userId, form);
        return "redirect:/admin/users/employers/" + userId + "/edit";
    }

    @PostMapping("/{userId}/approve")
    public String approve(@PathVariable Long userId) {
        employerService.approveEmployer(userId);
        return "redirect:/admin/users/employers";
    }

    @PostMapping("/{userId}/reject")
    public String reject(@PathVariable Long userId) {
        employerService.rejectEmployer(userId);
        return "redirect:/admin/users/employers";
    }
}
