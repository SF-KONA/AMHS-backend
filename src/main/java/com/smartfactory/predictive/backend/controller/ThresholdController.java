package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.ThresholdResponseDto;
import com.smartfactory.predictive.backend.dto.ThresholdUpdateRequestDto;
import com.smartfactory.predictive.backend.service.ThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thresholds")
@RequiredArgsConstructor
public class ThresholdController {

    private final ThresholdService thresholdService;

    @GetMapping
    public ResponseEntity<List<ThresholdResponseDto>> getAll() {
        return ResponseEntity.ok(thresholdService.getAll());
    }

    @PutMapping
    public ResponseEntity<List<ThresholdResponseDto>> updateAll(
            @RequestBody List<ThresholdUpdateRequestDto> dtos) {
        return ResponseEntity.ok(thresholdService.updateAll(dtos));
    }
}