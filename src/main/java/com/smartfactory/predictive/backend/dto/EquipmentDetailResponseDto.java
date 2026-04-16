package com.smartfactory.predictive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
public class EquipmentDetailResponseDto {
    private String deviceId;            // 장비 ID (OHT-02)
    private String deviceName;          // 장비명
    private String deviceType;          // 장비 유형 (OHT)
    private String manufacturer;        // 제조사
    private String installer;           // 설치자
    private LocalDate installDate;      // 설치일
    private String status;              // 현재 상태 (RUNNING)
    private Integer cumulativeOperatingDay; // 누적 가동일
    private String fabId;               // 공장 ID
    private String lineId;              // 소속 라인 (LINE-1)
}
