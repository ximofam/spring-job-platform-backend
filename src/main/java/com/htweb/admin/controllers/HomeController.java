package com.htweb.admin.controllers;

import com.htweb.core.enums.CompanyStatus;
import com.htweb.core.enums.CompanyType;
import com.htweb.core.enums.EmployeeSize;
import com.htweb.core.enums.Gender;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@ControllerAdvice
public class HomeController
{

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model) {
        return "admin/pages/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "admin/pages/dashboard";
    }

}
