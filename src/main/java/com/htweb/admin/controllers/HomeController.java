package com.htweb.admin.controllers;

import com.htweb.admin.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @Qualifier("adminCategoryService")
    private final CategoryService categoryService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "home";
    }
}
