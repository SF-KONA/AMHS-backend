package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    // SensorCheckScheduler용 - 장비별 최신 3개
    List<SensorReading> findTop3ByDeviceIdAndIdGreaterThanOrderByIdAsc(String deviceId, Long lastId);

    // 차트용 - 특정 장비 최신 60개
    List<SensorReading> findTop60ByDeviceIdOrderByCollectedAtDesc(String deviceId);

    // SensorPushScheduler용 - 특정 장비 최신 1개
    Optional<SensorReading> findTopByDeviceIdOrderByCollectedAtDesc(String deviceId);
}