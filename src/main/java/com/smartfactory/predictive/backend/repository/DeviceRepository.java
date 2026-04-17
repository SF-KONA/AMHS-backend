// domain/equipment/repository/DeviceRepository.java
package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.Device;
import com.smartfactory.predictive.backend.domain.enums.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    // 특정 상태의 장비 개수를 세는 쿼리 자동 생성
    long countByStatus(DeviceStatus status);

    List<Device> findByLineId(String lineId);
}