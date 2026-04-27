package com.htweb.admin.controllers;

import com.htweb.admin.repositories.PermissionRepository;
import com.htweb.admin.services.PermissionService;
import com.htweb.admin.services.RoleService;
import com.htweb.core.pojo.Permission;
import com.htweb.core.pojo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @GetMapping("/roles")
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/rolePage";
    }

    @GetMapping("/roles/{id}/edit")
    public String editRole(@PathVariable Long id, Model model) {
        Role role = roleService.findById(id);

        Set<Long> selectedPermIds = roleService.getPermissionIds(id);

        Map<String, List<Permission>> permsByModule = permissionService.findAllGroupedByModule();

        model.addAttribute("role", role);
        model.addAttribute("selectedPermIds", selectedPermIds);
        model.addAttribute("permsByModule", permsByModule);
        return "admin/roleEditPermPage";
    }

    @PostMapping("/roles/{id}/edit")
    public String saveRole(
            @PathVariable Long id,
            @RequestParam(required = false) List<Long> permissionIds,
            RedirectAttributes redirectAttrs) {

        roleService.updatePermissions(id, permissionIds);
        redirectAttrs.addFlashAttribute("success", "Đã lưu thay đổi!");
        return "redirect:/admin/roles/" + id + "/edit";
    }

    // POST /admin/roles/create — tạo mới
    @PostMapping("/roles/create")
    public String createRole(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttrs) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        this.roleService.save(role);
        redirectAttrs.addFlashAttribute("success", "Đã tạo vai trò: " + name);
        return "redirect:/admin/roles";
    }

    @GetMapping("/roles/{id}/update")
    public String showUpdateRole(@PathVariable Long id, Model model){
        model.addAttribute("role", roleService.findById(id));
        return "admin/roleUpdatePage";
    }

    @PostMapping("/roles/{id}/update")
    public String updateRole(@ModelAttribute(value = "role") Role role, RedirectAttributes redirectAttributes){
        this.roleService.update(role);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật: " + role.getName());
        return "redirect:/admin/roles/" + role.getId() + "/update";
    }

    @PostMapping("/roles/{id}/delete")
    public String deleteRole(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        this.roleService.delete(id);
        redirectAttrs.addFlashAttribute("success", "Đã xóa vai trò!");
        return "redirect:/admin/roles";
    }
}