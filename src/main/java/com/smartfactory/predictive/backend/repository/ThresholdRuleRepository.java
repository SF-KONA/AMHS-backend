package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.ThresholdRule;
import com.smartfactory.predictive.backend.domain.enums.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThresholdRuleRepository extends JpaRepository<ThresholdRule, Long> {

    // 장비 유형 + 센서명으로 임계값 조회
    Optional<ThresholdRule> findByDeviceTypeAndSensorName(DeviceType deviceType, String sensorName);
}