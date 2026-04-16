package com.smartfactory.predictive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 센서 차트(PM10, NTC, CT1, IR) 및 실시간 데이터 전송을 위한 DTO
 *
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataResponseDto {

    private Double pm10;      // 미세먼지 (μg/m³)

    private Double ntcTemp;   // NTC 온도 (°C) - 엔티티의 ntc 필드와 매핑

    private Double ct1Current; // CT1 전류 (A) - 엔티티의 ct1 필드와 매핑

    private Double irMaxTemp; // IR 최고온도 (°C) - 엔티티의 irTempMax 필드와 매핑

    private LocalDateTime timestamp; // 데이터 수집 시간 - 엔티티의 collectedAt 필드와 매핑
}
