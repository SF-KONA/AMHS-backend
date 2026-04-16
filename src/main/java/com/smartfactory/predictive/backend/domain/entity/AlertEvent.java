package com.smartfactory.predictive.backend.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert_event")
@Getter @Setter
@NoArgsConstructor
public class AlertEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @Column(name = "alert_level", nullable = false)
    private Byte alertLevel;

    @Column(name = "sensor_name", nullable = false, length = 20)
    private String sensorName;

    @Column(name = "sensor_value", nullable = false)
    private Double sensorValue;

    @Column(nullable = false)
    private Double threshold;

    @Column(nullable = false)
    private Boolean acknowledged = false;

    @Column(name = "ack_at")
    private LocalDateTime ackAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "device_id", nullable = false, length = 20)
    private String deviceId;
}