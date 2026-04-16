package com.smartfactory.predictive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class LineStatusResponseDto {
    private String lineId;           // 라인 ID
    private String deviceType;       // 장비 유형 (OHT 등)
    private String summaryStatus;    // 요약 (예: "가동 8 / 정지 1 / 정비 1")
    private List<DeviceSummaryDto> devices; // 라인 소속 장비 리스트

    @Getter @Setter
    @AllArgsConstructor
    public static class DeviceSummaryDto {
        private String deviceId;     // 장비 ID
        private String status;       // 현재 상태
    }
}