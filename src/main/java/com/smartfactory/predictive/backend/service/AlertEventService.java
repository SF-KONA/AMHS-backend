package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.domain.entity.AlertEvent;
import com.smartfactory.predictive.backend.dto.AlertEventResponse;
import com.smartfactory.predictive.backend.dto.AlertFilterRequest;
import com.smartfactory.predictive.backend.repository.AlertEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertEventService {

    private final AlertEventRepository alertEventRepository;

    // 알림 목록 조회 (필터)
    public List<AlertEventResponse> getAlerts(AlertFilterRequest filter) {
        List<AlertEvent> alerts = alertEventRepository.findByFilters(
                filter.getAlertLevel(),
                filter.getDeviceId(),
                filter.getAcknowledged()
        );

        return alerts.stream()
                .map(alert -> AlertEventResponse.builder()
                        .alertId(alert.getAlertId())
                        .createdAt(alert.getCreatedAt())
                        .deviceId(alert.getDeviceId())
                        .alertLevel(alert.getAlertLevel())
                        .sensorName(alert.getSensorName())
                        .sensorValue(alert.getSensorValue())
                        .acknowledged(alert.getAcknowledged())
                        .ackAt(alert.getAckAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 확인 처리 (미확인 → 확인 완료)
    @Transactional
    public void acknowledge(Long alertId) {
        AlertEvent alert = alertEventRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));

        alert.setAcknowledged(true);
        alert.setAckAt(LocalDateTime.now());
    }
}