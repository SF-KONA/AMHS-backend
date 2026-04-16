package com.smartfactory.predictive.backend.dto;

import com.smartfactory.predictive.backend.domain.enums.OrderStatus;
import com.smartfactory.predictive.backend.domain.enums.Priority;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class MaintOrderResponse {
    private String orderNumber;   // MO-001 형식
    private String deviceId;      // OHT-03
    private String lineId;        // LINE-1
    private Priority priority;
    private OrderStatus status;
    private LocalDateTime createdAt;
}