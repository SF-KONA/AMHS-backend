package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.dto.EquipmentDetailResponseDto;
import com.smartfactory.predictive.backend.dto.EquipmentListResponseDto;
import com.smartfactory.predictive.backend.dto.SensorDataResponseDto;
import com.smartfactory.predictive.backend.repository.DeviceRepository;
import com.smartfactory.predictive.backend.repository.SensorReadingRepository; // 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final DeviceRepository deviceRepository;
    private final SensorReadingRepository sensorReadingRepository;

    /**
     * 장비 목록 조회 (필터링 조건 적용)
     */
    @Transactional(readOnly = true)
    public EquipmentListResponseDto getEquipments(String fabId, String lineId, String status) {
        List<EquipmentListResponseDto.EquipmentSummaryDto> filtered = deviceRepository.findAll().stream()
                .filter(d -> (fabId == null || fabId.isEmpty() || d.getFabId().equals(fabId)))
                .filter(d -> (lineId == null || lineId.isEmpty() || d.getLineId().equals(lineId)))
                .filter(d -> (status == null || status.isEmpty() || d.getStatus().name().equals(status)))
                .map(d -> new EquipmentListResponseDto.EquipmentSummaryDto(
                        d.getDeviceId(),
                        d.getDeviceType().name(),
                        d.getLineId(),
                        d.getCumulativeOperatingDay(),
                        d.getStatus().name(),
                        d.getDeviceName(),
                        d.getDeviceManufacturer()
                ))
                .collect(Collectors.toList());

        return new EquipmentListResponseDto(filtered, filtered.size());
    }

    /**
     * 장비 상세 정보 조회
     */
    @Transactional(readOnly = true)
    public EquipmentDetailResponseDto getEquipmentDetail(String id) {
        return deviceRepository.findById(id)
                .map(d -> new EquipmentDetailResponseDto(
                        d.getDeviceId(),
                        d.getDeviceName(),
                        d.getDeviceType().name(),
                        d.getDeviceManufacturer(),
                        d.getInstaller(),
                        d.getInstallDate(),
                        d.getStatus().name(),
                        d.getCumulativeOperatingDay(),
                        d.getFabId(),
                        d.getLineId()
                ))
                .orElseThrow(() -> new RuntimeException("해당 장비를 찾을 수 없습니다: " + id));
    }

    /**
     *특정 장비의 최근 센서 데이터 60건 조회 (차트용)
     *
     */
    @Transactional(readOnly = true)
    public List<SensorDataResponseDto> getRecentSensorData(String id) {
        // Repository에 추가한 findTop60ByDeviceIdOrderByCollectedAtDesc 메서드를 사용합니다.
        return sensorReadingRepository.findTop60ByDeviceIdOrderByCollectedAtDesc(id).stream()
                .map(s -> new SensorDataResponseDto(
                        s.getPm10(),      // 미세먼지
                        s.getNtc(),       // 온도 (엔티티 필드명: ntc)
                        s.getCt1(),       // 전류 (엔티티 필드명: ct1)
                        s.getIrTempMax(),  // IR 최고온도 (엔티티 필드명: irTempMax)
                        s.getCollectedAt() // 데이터 수집 시간
                ))
                .collect(Collectors.toList());
    }
}