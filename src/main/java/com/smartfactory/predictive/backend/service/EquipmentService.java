package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.dto.EquipmentDetailResponseDto;
import com.smartfactory.predictive.backend.dto.EquipmentListResponseDto;
import com.smartfactory.predictive.backend.dto.SensorDataResponseDto;
import com.smartfactory.predictive.backend.repository.DeviceRepository;
import com.smartfactory.predictive.backend.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
     * 특정 장비의 최근 센서 데이터 60건 조회 (차트용)
     */
    @Transactional(readOnly = true)
    public List<SensorDataResponseDto> getRecentSensorData(String id) {
        return sensorReadingRepository.findTop60ByDeviceIdOrderByCollectedAtDesc(id).stream()
                .map(s -> new SensorDataResponseDto(
                        s.getPm10(),
                        s.getNtc(),
                        s.getCt1(),
                        s.getIrTempMax(),
                        s.getCollectedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 특정 장비의 최신 센서 데이터 1건 조회 (실시간 push용)
     */
    @Transactional(readOnly = true)
    public Optional<SensorDataResponseDto> getLatestSensorData(String deviceId) {
        return sensorReadingRepository
                .findTopByDeviceIdOrderByCollectedAtDesc(deviceId)
                .map(s -> new SensorDataResponseDto(
                        s.getPm10(),
                        s.getNtc(),
                        s.getCt1(),
                        s.getIrTempMax(),
                        s.getCollectedAt()
                ));
    }
}