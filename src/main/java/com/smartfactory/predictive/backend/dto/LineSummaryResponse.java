package com.smartfactory.predictive.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineSummaryResponse {
    private String lineId;
    private int totalSlots;
    private int runningCount;
    private double utilizationRate;  // 가동률
    private long pendingOrderCount;  // 미처리 오더 수
}