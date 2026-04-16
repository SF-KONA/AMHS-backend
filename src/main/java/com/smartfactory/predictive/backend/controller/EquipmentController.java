package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.EquipmentListResponseDto;
import com.smartfactory.predictive.backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.smartfactory.predictive.backend.dto.EquipmentDetailResponseDto;
import com.smartfactory.predictive.backend.dto.SensorDataResponseDto;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "장비 및 센서 API", description = "장비 목록, 상세 조회 및 센서 이력 조회를 담당합니다.") // [추가]
@RestController
@RequestMapping("/api/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Operation(summary = "장비 목록 조회", description = "Fab, Line, 상태별로 필터링된 장비 목록을 가져옵니다.")
    @GetMapping
    public EquipmentListResponseDto getEquipments(
            @RequestParam(name = "fabId", required = false) String fabId,
            @RequestParam(name = "lineId", required = false) String lineId,
            @RequestParam(name = "status", required = false) String status) {
        //
        return equipmentService.getEquipments(fabId, lineId, status);
    }

    @Operation(summary = "장비 상세 정보 조회", description = "특정 장비의 마스터 정보 10종을 조회합니다.") // [추가]
    @GetMapping("/{id}")
    public EquipmentDetailResponseDto getEquipmentDetail(@PathVariable(name = "id") String id) {
        return equipmentService.getEquipmentDetail(id);
    }

    @Operation(summary = "센서 차트 이력 조회", description = "특정 장비의 최신 센서 데이터 60건을 조회합니다.") // [추가]
    @GetMapping("/{id}/sensors/history")
    public List<SensorDataResponseDto> getSensorHistory(@PathVariable(name = "id") String id) {
        return equipmentService.getRecentSensorData(id);
    }
}