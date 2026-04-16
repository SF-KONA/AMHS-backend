package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.EquipmentListResponseDto;
import com.smartfactory.predictive.backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.smartfactory.predictive.backend.dto.EquipmentDetailResponseDto;
import com.smartfactory.predictive.backend.dto.SensorDataResponseDto;
import java.util.List;

@RestController
@RequestMapping("/api/equipments") // 소문자 복수형
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public EquipmentListResponseDto getEquipments(
            @RequestParam(required = false) String fabId,
            @RequestParam(required = false) String lineId,
            @RequestParam(required = false) String status) {
        return equipmentService.getEquipments(fabId, lineId, status);
    }

    @GetMapping("/{id}")
    public EquipmentDetailResponseDto getEquipmentDetail(@PathVariable(name = "id") String id) {
        return equipmentService.getEquipmentDetail(id);
    }

    // 특정 장비의 최근 센서 기록 60건 조회 API
    @GetMapping("/{id}/sensors/history")
    public List<SensorDataResponseDto> getSensorHistory(@PathVariable(name = "id") String id) {
        return equipmentService.getRecentSensorData(id);
    }
}