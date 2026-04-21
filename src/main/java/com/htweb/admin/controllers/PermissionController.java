package com.htweb.admin.controllers;

import com.htweb.admin.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PermissionController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/permission")
    public String index(Model model){
        model.addAttribute("roles",this.roleService.findAll());
        return "admin/permission";
    }
}
