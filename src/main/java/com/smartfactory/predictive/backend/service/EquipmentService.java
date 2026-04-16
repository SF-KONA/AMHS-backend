package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.dto.EquipmentListResponseDto;
import com.smartfactory.predictive.backend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public EquipmentListResponseDto getEquipments(String fabId, String lineId, String status) {
        // 1. 모든 장비를 가져와서 필터링 조건을 적용합니다
        List<EquipmentListResponseDto.EquipmentSummaryDto> filtered = deviceRepository.findAll().stream()
                .filter(d -> (fabId == null || fabId.isEmpty() || d.getFabId().equals(fabId)))   // Fab 필터
                .filter(d -> (lineId == null || lineId.isEmpty() || d.getLineId().equals(lineId))) // Line 필터
                .filter(d -> (status == null || status.isEmpty() || d.getStatus().name().equals(status))) // 상태 필터
                .map(d -> new EquipmentListResponseDto.EquipmentSummaryDto(
                        d.getDeviceId(),
                        d.getDeviceType().name(),
                        d.getLineId(),
                        d.getStatus().name(),
                        d.getDeviceName(),
                        d.getDeviceManufacturer()
                ))
                .collect(Collectors.toList());

        return new EquipmentListResponseDto(filtered, filtered.size());
    }
}