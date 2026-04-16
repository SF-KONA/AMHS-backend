package com.smartfactory.predictive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 대시보드 상단 요약 카드 데이터를 위한 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {
    private long runningCount;      // 가동 장비 수
    private long stoppedCount;      // 정지 장비 수
    private long maintenanceCount;  // 정비 중 장비 수
    private long totalCount;        // 전체 장비 수
}