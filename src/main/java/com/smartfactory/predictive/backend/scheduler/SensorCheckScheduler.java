package com.smartfactory.predictive.backend.scheduler;

import com.smartfactory.predictive.backend.domain.entity.AlertEvent;
import com.smartfactory.predictive.backend.domain.entity.MaintOrder;
import com.smartfactory.predictive.backend.domain.entity.SensorReading;
import com.smartfactory.predictive.backend.domain.entity.ThresholdRule;
import com.smartfactory.predictive.backend.domain.enums.DeviceType;
import com.smartfactory.predictive.backend.domain.enums.OrderType;
import com.smartfactory.predictive.backend.domain.enums.Priority;
import com.smartfactory.predictive.backend.repository.AlertEventRepository;
import com.smartfactory.predictive.backend.repository.MaintOrderRepository;
import com.smartfactory.predictive.backend.repository.SensorReadingRepository;
import com.smartfactory.predictive.backend.repository.ThresholdRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorCheckScheduler {

    private final SensorReadingRepository sensorReadingRepository;
    private final ThresholdRuleRepository thresholdRuleRepository;
    private final AlertEventRepository alertEventRepository;
    private final MaintOrderRepository maintOrderRepository;

    private Long lastProcessedId = 0L; // 마지막으로 처리한 sensor_reading_id

    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    @Transactional
    public void checkSensor() {
        // 1. 마지막 처리 id 이후 10개 가져오기
        List<SensorReading> readings = sensorReadingRepository
                .findTop10ByIdGreaterThanOrderByIdAsc(lastProcessedId);

        if (readings.isEmpty()) {
            log.info("새로운 센서 데이터 없음");
            return;
        }

        for (SensorReading reading : readings) {
            // 2. deviceId로 deviceType 추출 (oht01 → OHT)
            String deviceType = reading.getDeviceId().substring(0, 3).toUpperCase();
            DeviceType type = DeviceType.valueOf(deviceType);

            // 3. 각 센서값 체크
            checkAndAlert(reading, type, "PM10", reading.getPm10());
            checkAndAlert(reading, type, "PM2P5", reading.getPm2p5());
            checkAndAlert(reading, type, "PM1P0", reading.getPm1p0());
            checkAndAlert(reading, type, "NTC", reading.getNtc());
            checkAndAlert(reading, type, "CT1", reading.getCt1());
            checkAndAlert(reading, type, "CT2", reading.getCt2());
            checkAndAlert(reading, type, "CT3", reading.getCt3());
            checkAndAlert(reading, type, "CT4", reading.getCt4());
            checkAndAlert(reading, type, "IR_TEMP_MAX", reading.getIrTempMax());
        }

        // 4. 마지막 처리 id 업데이트
        lastProcessedId = readings.get(readings.size() - 1).getId();
        log.info("마지막 처리 id: {}", lastProcessedId);
    }

    private void checkAndAlert(SensorReading reading, DeviceType type, String sensorName, Double value) {
        // 센서값 없으면 스킵
        if (value == null) return;

        // 임계값 조회
        Optional<ThresholdRule> ruleOpt = thresholdRuleRepository
                .findByDeviceTypeAndSensorName(type, sensorName);
        if (ruleOpt.isEmpty()) return;

        ThresholdRule rule = ruleOpt.get();

        // 임계값 비교 → 등급 결정
        Byte alertLevel = null;
        double threshold = 0;

        if (value >= rule.getWarnLevel3()) {
            alertLevel = 3; // 위험
            threshold = rule.getWarnLevel3();
        } else if (value >= rule.getWarnLevel2()) {
            alertLevel = 2; // 경고
            threshold = rule.getWarnLevel2();
        } else if (value >= rule.getWarnLevel1()) {
            alertLevel = 1; // 관심
            threshold = rule.getWarnLevel1();
        }

        // 임계값 안 넘으면 스킵
        if (alertLevel == null) return;

        // 알림 생성
        AlertEvent alert = new AlertEvent();
        alert.setAlertLevel(alertLevel);
        alert.setSensorName(sensorName);
        alert.setSensorValue(value);
        alert.setThreshold(threshold);
        alert.setDeviceId(reading.getDeviceId());
        AlertEvent savedAlert = alertEventRepository.save(alert);

        log.info("알림 생성 - 장비: {}, 센서: {}, 값: {}, 등급: {}",
                reading.getDeviceId(), sensorName, value, alertLevel);

        // 정비 오더 생성
        MaintOrder order = new MaintOrder();
        order.setAlertId(savedAlert.getAlertId());
        order.setOrderType(OrderType.CORRECTIVE);
        order.setPriority(alertLevel == 3 ? Priority.HIGH : alertLevel == 2 ? Priority.MEDIUM : Priority.LOW);
        order.setTitle("[" + reading.getDeviceId() + "] " + sensorName + " 임계값 초과");
        order.setDescription("센서값: " + value + " / 임계값: " + threshold);
        maintOrderRepository.save(order);

        log.info("정비 오더 생성 - 장비: {}, 센서: {}", reading.getDeviceId(), sensorName);
    }
}