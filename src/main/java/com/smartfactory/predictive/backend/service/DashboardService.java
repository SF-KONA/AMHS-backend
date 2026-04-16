package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.dto.DashboardSummaryDto;
import com.smartfactory.predictive.backend.dto.LineStatusResponseDto; // DTO 추가
import com.smartfactory.predictive.backend.domain.enums.DeviceStatus;
import com.smartfactory.predictive.backend.repository.ProductionLineRepository; // Repository 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smartfactory.predictive.backend.repository.DeviceRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DeviceRepository deviceRepository;
    private final ProductionLineRepository lineRepository; // 1. 라인 레포지토리 주입

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        long running = deviceRepository.countByStatus(DeviceStatus.RUNNING);
        long stopped = deviceRepository.countByStatus(DeviceStatus.STOPPED);
        long maintenance = deviceRepository.countByStatus(DeviceStatus.MAINTENANCE);
        long total = deviceRepository.count();

        return new DashboardSummaryDto(running, stopped, maintenance, total);
    }

    // 2. 새로운 MVP 기능: 라인별 가동 현황 조회
    @Transactional(readOnly = true)
    public List<LineStatusResponseDto> getLineStatus(String fabId) {
        // 모든 라인을 가져온 뒤, 요청받은 Fab(A 또는 B)에 속한 라인만 필터링합니다.
        return lineRepository.findAll().stream()
                .filter(line -> line.getFabId().equals(fabId))
                .map(line -> {
                    // 해당 라인에 소속된 장비들을 찾아 DTO로 변환합니다.
                    List<LineStatusResponseDto.DeviceSummaryDto> devices = deviceRepository.findAll().stream()
                            .filter(d -> d.getLineId().equals(line.getLineId()))
                            .map(d -> new LineStatusResponseDto.DeviceSummaryDto(d.getDeviceId(), d.getStatus().name()))
                            .collect(Collectors.toList());

                    // 화면 우측 상단에 표시될 요약 텍스트를 만듭니다.
                    String summary = String.format("가동 %d / 정지 %d / 정비 %d",
                            devices.stream().filter(d -> d.getStatus().equals("RUNNING")).count(),
                            devices.stream().filter(d -> d.getStatus().equals("STOPPED")).count(),
                            devices.stream().filter(d -> d.getStatus().equals("MAINTENANCE")).count());

                    return new LineStatusResponseDto(line.getLineId(), line.getDeviceType().name(), summary, devices);
                })
                .collect(Collectors.toList());
    }
}