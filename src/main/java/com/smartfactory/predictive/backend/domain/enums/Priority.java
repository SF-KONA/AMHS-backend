package com.smartfactory.predictive.backend.domain.enums;

import lombok.Getter;

@Getter
public enum Priority {
    HIGH("높음"),
    MEDIUM("중간"),
    LOW("낮음");

    private final String description;

    Priority(String description) {
        this.description = description;
    }
}