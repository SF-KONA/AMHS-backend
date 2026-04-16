package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    // 마지막으로 처리한 id 이후 10개 가져오기
    List<SensorReading> findTop10ByIdGreaterThanOrderByIdAsc(Long lastId);
}