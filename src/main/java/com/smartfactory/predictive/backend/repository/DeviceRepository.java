package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Device의 PK(기본키)는 String 타입(예: OHT-01)이므로 두 번째 자리에 String을 넣습니다.
@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
}