package com.smartfactory.predictive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class EquipmentListResponseDto {
    private List<EquipmentSummaryDto> equipments; // 장비 리스트
    private long totalElements;                  // 전체 장비 수

    @Getter @Setter
    @AllArgsConstructor
    public static class EquipmentSummaryDto {
        private String deviceId;
        private String deviceType;
        private String lineId;
        private Integer cumulativeOperatingDay; // 추가: 누적 가동일
        private String status;
        private String deviceName;
        private String manufacturer;
    }
}