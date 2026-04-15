// domain/dashboard/controller/DashboardController.java
package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.DashboardSummaryDto;
import com.smartfactory.predictive.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard") // 소문자 + kebab-case
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary") // API 경로명 소문자
    public DashboardSummaryDto getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }
}
