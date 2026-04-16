package com.smartfactory.predictive.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 상단 드롭다운 필터값 → 서버로 보냄
public class AlertFilterRequest {
    private Byte alertLevel;      // 등급: 1/2/3
    private String deviceId;      // 장비: OHT-03
    private Boolean acknowledged; // 확인여부: true/false
}