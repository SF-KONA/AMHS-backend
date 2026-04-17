package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.domain.entity.Device;
import com.smartfactory.predictive.backend.domain.entity.ProductionLine;
import com.smartfactory.predictive.backend.domain.enums.DeviceStatus;
import com.smartfactory.predictive.backend.dto.DashboardSummaryDto;
import com.smartfactory.predictive.backend.dto.LineStatusResponseDto;
import com.smartfactory.predictive.backend.repository.DeviceRepository;
import com.smartfactory.predictive.backend.repository.ProductionLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DeviceRepository deviceRepository;
    private final ProductionLineRepository lineRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        long running = deviceRepository.countByStatus(DeviceStatus.RUNNING);
        long stopped = deviceRepository.countByStatus(DeviceStatus.STOPPED);
        long maintenance = deviceRepository.countByStatus(DeviceStatus.MAINTENANCE);
        long total = deviceRepository.count();

        return new DashboardSummaryDto(running, stopped, maintenance, total);
    }

    @Transactional(readOnly = true)
    public List<LineStatusResponseDto> getLineStatus(String fabId) {
        List<ProductionLine> lines = lineRepository.findByFabId(fabId);

        return lines.stream()
                .map(line -> {
                    List<Device> lineDevices = deviceRepository.findByLineId(line.getLineId());

                    long runningCount = lineDevices.stream()
                            .filter(device -> device.getStatus() == DeviceStatus.RUNNING)
                            .count();

                    long stoppedCount = lineDevices.stream()
                            .filter(device -> device.getStatus() == DeviceStatus.STOPPED)
                            .count();

                    long maintenanceCount = lineDevices.stream()
                            .filter(device -> device.getStatus() == DeviceStatus.MAINTENANCE)
                            .count();

                    // 🔥 핵심: DB값이 아니라 실제 장비 수 사용
                    int totalSlots = lineDevices.size();

                    int minRunning = line.getMinRunning() != null ? line.getMinRunning() : 0;

                    // 안전장치 (추천)
                    if (minRunning > totalSlots) {
                        minRunning = totalSlots;
                    }

                    List<LineStatusResponseDto.DeviceSummaryDto> devices =
                            lineDevices.stream()
                                    .map(device -> new LineStatusResponseDto.DeviceSummaryDto(
                                            device.getDeviceId(),
                                            device.getStatus().name()
                                    ))
                                    .toList();

                    String summaryStatus = String.format(
                            "가동 %d / 정지 %d / 정비 %d",
                            runningCount,
                            stoppedCount,
                            maintenanceCount
                    );

                    return new LineStatusResponseDto(
                            line.getLineId(),
                            line.getDeviceType().name(),
                            totalSlots,
                            minRunning,
                            runningCount,
                            stoppedCount,
                            maintenanceCount,
                            summaryStatus,
                            devices
                    );
                })
                .toList();
    }
}