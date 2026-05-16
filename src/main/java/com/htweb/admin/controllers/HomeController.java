package com.htweb.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HomeController
{
    @GetMapping("/")
    public String home(Model model) {
        return "admin/pages/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "admin/pages/dashboard";
    }

    @GetMapping("/user")
    public String user(Model model) {
        return "admin/pages/user";
    }


    @GetMapping("/role")
    public String role(Model model) {
        return "admin/pages/role";
    }
}
