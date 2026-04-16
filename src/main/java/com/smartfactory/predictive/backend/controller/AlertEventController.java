package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.AlertEventResponse;
import com.smartfactory.predictive.backend.dto.AlertFilterRequest;
import com.smartfactory.predictive.backend.service.AlertEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertEventController {

    private final AlertEventService alertEventService;

    // 알림 목록 조회 (필터)
    @GetMapping
    public ResponseEntity<List<AlertEventResponse>> getAlerts(
            @ModelAttribute AlertFilterRequest filter) {
        return ResponseEntity.ok(alertEventService.getAlerts(filter));
    }

    // 확인 처리
    @PatchMapping("/{alertId}/ack")
    public ResponseEntity<Void> acknowledge(@PathVariable Long alertId) {
        alertEventService.acknowledge(alertId);
        return ResponseEntity.ok().build();
    }
}