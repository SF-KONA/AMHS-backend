package com.smartfactory.predictive.backend.dto;

import com.smartfactory.predictive.backend.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaintOrderFilterRequest {
    private String lineId;
    private OrderStatus status;
}