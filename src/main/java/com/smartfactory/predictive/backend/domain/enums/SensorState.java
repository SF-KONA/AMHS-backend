package com.smartfactory.predictive.backend.domain.enums;

import lombok.Getter;

@Getter
public enum SensorState {
    NORMAL(0, "정상"),
    INTEREST(1, "관심"),
    WARN(2, "경고"),
    DANGER(3, "위험");

    private final int value;
    private final String description;

    SensorState(int value, String description) {
        this.value = value;
        this.description = description;
    }
}