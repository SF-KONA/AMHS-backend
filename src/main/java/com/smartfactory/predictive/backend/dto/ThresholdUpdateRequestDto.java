package com.smartfactory.predictive.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ThresholdUpdateRequestDto {

    private Long ruleId;
    private Double idle;      // warnLevel1 (관심)
    private Double warning;   // warnLevel2 (경고)
    private Double error;     // warnLevel3 (위험)
}