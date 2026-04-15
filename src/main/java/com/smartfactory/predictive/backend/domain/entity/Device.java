package com.smartfactory.predictive.backend.domain.entity;

import com.smartfactory.predictive.backend.domain.enums.DeviceStatus;
import com.smartfactory.predictive.backend.domain.enums.DeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "device")
@Getter @Setter
@NoArgsConstructor
public class Device {

    @Id
    @Column(name = "device_id", length = 20)
    private String deviceId;

    @Column(name = "device_manufacturer", nullable = false, length = 50)
    private String deviceManufacturer;

    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(length = 50)
    private String installer;

    @Column(name = "install_date")
    private LocalDate installDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status = DeviceStatus.RUNNING;

    @Column(name = "cumulative_operating_day", nullable = false)
    private Integer cumulativeOperatingDay = 0;

    @Column(name = "fab_id", nullable = false, length = 10)
    private String fabId;

    @Column(name = "line_id", nullable = false, length = 10)
    private String lineId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}