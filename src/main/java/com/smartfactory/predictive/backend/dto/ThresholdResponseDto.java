package com.smartfactory.predictive.backend.dto;

import com.smartfactory.predictive.backend.domain.entity.ThresholdRule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ThresholdResponseDto {

    private Long ruleId;
    private String deviceType;
    private String sensorName;
    private Double idle;      // warnLevel1 (관심)
    private Double warning;   // warnLevel2 (경고)
    private Double error;     // warnLevel3 (위험)

    public static ThresholdResponseDto from(ThresholdRule rule) {
        ThresholdResponseDto dto = new ThresholdResponseDto();
        dto.setRuleId(rule.getRuleId());
        dto.setDeviceType(rule.getDeviceType().name());
        dto.setSensorName(rule.getSensorName());
        dto.setIdle(rule.getWarnLevel1());
        dto.setWarning(rule.getWarnLevel2());
        dto.setError(rule.getWarnLevel3());
        return dto;
    }
}