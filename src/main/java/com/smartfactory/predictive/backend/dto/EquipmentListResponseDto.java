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
        private String deviceId;        // 장비코드
        private String deviceType;      // 장비유형
        private String lineId;          // 라인 ID
        private String status;          // 현재 상태
        private String deviceName;      // 장비이름
        private String manufacturer;    // 제조사
    }
}