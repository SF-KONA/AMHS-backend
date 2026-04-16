package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.EquipmentListResponseDto;
import com.smartfactory.predictive.backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}