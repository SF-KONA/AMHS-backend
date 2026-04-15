package com.smartfactory.predictive.backend.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_reading")
@Getter @Setter
@NoArgsConstructor
public class SensorReading {

    @Id
    @Column(name = "sensor_reading_id")
    private Long id;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    private Double pm10;
    private Double pm2p5;
    private Double pm1p0;
    private Double ntc;
    private Double ct1;
    private Double ct2;
    private Double ct3;
    private Double ct4;

    @Column(name = "ir_temp_max")
    private Double irTempMax;

    @Column(name = "x_tmax")
    private Integer xTmax;

    @Column(name = "y_tmax")
    private Integer yTmax;

    // DB에 TINYINT로 되어 있어 Byte로 받습니다.
    // 나중에 화면에 보여줄 때 Enum(SensorState)과 매칭하면 편합니다.
    private Byte state;

    @Column(name = "ex_temp")
    private Double exTemp;

    @Column(name = "ex_humidity")
    private Double exHumidity;

    @Column(name = "ex_lux")
    private Double exLux;

    @Column(name = "ir_image_path")
    private String irImagePath;

    @Column(name = "device_id", nullable = false, length = 20)
    private String deviceId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}