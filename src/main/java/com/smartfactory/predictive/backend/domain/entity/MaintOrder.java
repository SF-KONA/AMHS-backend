package com.smartfactory.predictive.backend.domain.entity;

import com.smartfactory.predictive.backend.domain.enums.OrderStatus;
import com.smartfactory.predictive.backend.domain.enums.OrderType;
import com.smartfactory.predictive.backend.domain.enums.Priority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "maint_order")
@Getter @Setter
@NoArgsConstructor
public class MaintOrder {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.OPEN;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String assignee;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "alert_id", nullable = false)
    private Long alertId;
}