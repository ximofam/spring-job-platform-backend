/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.htweb.admin.controllers;

import com.htweb.admin.enums.StatisticPeriod;
import com.htweb.admin.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author _H₂SO₄_
 */
@Controller
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class StatisticsController {
    @Qualifier("adminStatisticsService")
    private final StatisticsService statisticsService;

    @GetMapping("/activity")
    public String statsActivity(
            @RequestParam(defaultValue = "MONTH") StatisticPeriod period,
            @RequestParam(defaultValue = "2026") Integer year,
            @RequestParam(required = false) Integer value,
            Model model
    ) {
        model.addAttribute("pendingCompany",statisticsService.countPendingCompanies(period, value,year));
        model.addAttribute("newUser",statisticsService.countNewUsersByRole(period, value,year));
        model.addAttribute("chartData",statisticsService.countNewUsersByRoleGrouped(period, year));
        model.addAttribute("companyStatusData",statisticsService.countCompaniesByStatus(period, value, year));
        model.addAttribute("jobData",statisticsService.countNewJobsGrouped(period, year));

        model.addAttribute("currentPeriod", period);
        model.addAttribute("currentYear", year);
        model.addAttribute("currentValue", value);

        return "admin/pages/stats_activity";
    }
    @GetMapping("/market")
    public String statsMarket(
            @RequestParam(defaultValue = "MONTH") StatisticPeriod period,
            @RequestParam(defaultValue = "2026") Integer year,
            @RequestParam(required = false) Integer value,
            Model model
    ) {
        model.addAttribute("categoryData", statisticsService.countJobsByCategory(period, value, year));
        model.addAttribute("appStatusData", statisticsService.countApplicationsByStatus(period, value, year));
        model.addAttribute("currentPeriod", period);
        model.addAttribute("currentYear", year);
        model.addAttribute("currentValue", value);
        return "admin/pages/stats_market";
    }

    @GetMapping("/revenue")
    public String statsRevenue(
            @RequestParam(defaultValue = "MONTH") StatisticPeriod period,
            @RequestParam(defaultValue = "2026") Integer year,
            Model model
    ) {
        model.addAttribute("revenueData", statisticsService.revenueGrouped(period, year));
        model.addAttribute("topCompanies", statisticsService.topCompaniesByRevenue(period, year));
        model.addAttribute("currentPeriod", period);
        model.addAttribute("currentYear", year);
        return "admin/pages/stats_revenue";
    }
}
