package com.smartfactory.predictive.backend.domain.entity;

import com.smartfactory.predictive.backend.domain.enums.DeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "threshold_rule")
@Getter @Setter
@NoArgsConstructor
public class ThresholdRule {

    @Id
    @Column(name = "rule_id")
    private Long ruleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "sensor_name", nullable = false, length = 20)
    private String sensorName;

    @Column(name = "warn_level1", nullable = false)
    private Double warnLevel1;

    @Column(name = "warn_level2", nullable = false)
    private Double warnLevel2;

    @Column(name = "warn_level3", nullable = false)
    private Double warnLevel3;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}