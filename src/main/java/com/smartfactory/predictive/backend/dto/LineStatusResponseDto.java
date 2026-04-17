package com.smartfactory.predictive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LineStatusResponseDto {

    private String lineId;
    private String deviceType;
    private int totalSlots;          // 전체 장비 수 (확정)
    private int minRunning;
    private long runningCount;
    private long stoppedCount;
    private long maintenanceCount;
    private String summaryStatus;
    private List<DeviceSummaryDto> devices;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DeviceSummaryDto {
        private String deviceId;
        private String status;
    }
}