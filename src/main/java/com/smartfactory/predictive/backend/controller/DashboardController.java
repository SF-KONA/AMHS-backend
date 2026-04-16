package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.DashboardSummaryDto;
import com.smartfactory.predictive.backend.dto.LineStatusResponseDto;
import com.smartfactory.predictive.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummaryDto getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }

    /**
     * 라인별 가동 현황 API
     * 예: GET /api/dashboard/line-status?fabId=FAB-A
     */
    @GetMapping("/line-status")
    public List<LineStatusResponseDto> getLineStatus(@RequestParam(name = "fabId") String fabId) {
        return dashboardService.getLineStatus(fabId);
    }
}