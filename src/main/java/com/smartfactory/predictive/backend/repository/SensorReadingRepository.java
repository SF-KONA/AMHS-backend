package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    // 💡 JpaRepository를 상속받는 것만으로도 DB 조회, 저장, 삭제 기능이 자동으로 완성됩니다!
}