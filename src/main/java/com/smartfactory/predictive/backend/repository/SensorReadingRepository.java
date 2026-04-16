package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    //
    List<SensorReading> findTop10ByIdGreaterThanOrderByIdAsc(Long lastId);

    // 특정 장비의 최신 센서 데이터 60개를 수집 시간 내림차순으로 조회
    List<SensorReading> findTop60ByDeviceIdOrderByCollectedAtDesc(String deviceId);
}

