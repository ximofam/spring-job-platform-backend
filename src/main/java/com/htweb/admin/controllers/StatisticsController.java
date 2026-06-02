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
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {
    @Qualifier("adminStatisticsService")
    private final StatisticsService statisticsService;

    @GetMapping("/companies/pending")
    public ResponseEntity<?> countPendingCompanies(
            @RequestParam StatisticPeriod period,
            @RequestParam(required = false) Integer value,
            @RequestParam(required = false) Integer year
    ) {
        long count = statisticsService.countPendingCompanies(period, value, year);
        Map<String, Object> response = new HashMap<>();
        response.put("period", period);
        response.put("value", value);
        response.put("year", year);
        response.put("count", count);

        return ResponseEntity.ok(response);
    }

    
}
