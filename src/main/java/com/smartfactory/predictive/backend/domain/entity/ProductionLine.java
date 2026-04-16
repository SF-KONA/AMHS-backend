package com.smartfactory.predictive.backend.domain.entity;

import com.smartfactory.predictive.backend.domain.enums.DeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_line")
@Getter @Setter
@NoArgsConstructor
public class ProductionLine {

    @Id
    @Column(name = "line_id", length = 10)
    private String lineId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots = 10;

    @Column(name = "min_running", nullable = false)
    private Integer minRunning = 7;

    @Column(name = "fab_id", nullable = false, length = 10)
    private String fabId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}