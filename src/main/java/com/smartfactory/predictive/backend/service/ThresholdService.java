package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.domain.entity.ThresholdRule;
import com.smartfactory.predictive.backend.dto.ThresholdResponseDto;
import com.smartfactory.predictive.backend.dto.ThresholdUpdateRequestDto;
import com.smartfactory.predictive.backend.repository.ThresholdRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThresholdService {

    private final ThresholdRuleRepository thresholdRuleRepository;

    public List<ThresholdResponseDto> getAll() {
        return thresholdRuleRepository.findAll()
                .stream()
                .map(ThresholdResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ThresholdResponseDto> updateAll(List<ThresholdUpdateRequestDto> dtos) {
        for (ThresholdUpdateRequestDto dto : dtos) {
            ThresholdRule rule = thresholdRuleRepository.findById(dto.getRuleId())
                    .orElseThrow(() -> new RuntimeException("임계값 규칙을 찾을 수 없습니다: " + dto.getRuleId()));

            rule.setWarnLevel1(dto.getIdle());
            rule.setWarnLevel2(dto.getWarning());
            rule.setWarnLevel3(dto.getError());
            rule.setUpdatedAt(LocalDateTime.now());
        }
        return thresholdRuleRepository.findAll()
                .stream()
                .map(ThresholdResponseDto::from)
                .collect(Collectors.toList());
    }
}