package com.smartfactory.predictive.backend.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class AlertEventResponse {
    private Long alertId;
    private String deviceId;
    private Byte alertLevel;
    private String sensorName;
    private Double sensorValue;
    private Boolean acknowledged;
    private LocalDateTime createdAt;
    private LocalDateTime ackAt;
}