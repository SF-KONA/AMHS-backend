package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.AlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertEventRepository extends JpaRepository<AlertEvent, Long> {

    @Query("SELECT a FROM AlertEvent a WHERE " +
            "(:alertLevel IS NULL OR a.alertLevel = :alertLevel) AND " +
            "(:deviceId IS NULL OR a.deviceId = :deviceId) AND " +
            "(:acknowledged IS NULL OR a.acknowledged = :acknowledged) " +
            "ORDER BY a.createdAt DESC")
    List<AlertEvent> findByFilters(
            @Param("alertLevel") Byte alertLevel,
            @Param("deviceId") String deviceId,
            @Param("acknowledged") Boolean acknowledged);

    // 중복 알림 방지용 - 미확인 알림 존재 여부 확인
    boolean existsByDeviceIdAndSensorNameAndAcknowledgedFalse(
            String deviceId, String sensorName);
}