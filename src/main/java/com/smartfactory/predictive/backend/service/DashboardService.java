// domain/dashboard/service/DashboardService.java
package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.dto.DashboardSummaryDto;
import com.smartfactory.predictive.backend.domain.enums.DeviceStatus;
import com.smartfactory.predictive.backend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 생성자 주입 자동 생성
public class DashboardService {

    private final DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() { // camelCase
        long running = deviceRepository.countByStatus(DeviceStatus.RUNNING);
        long stopped = deviceRepository.countByStatus(DeviceStatus.STOPPED);
        long maintenance = deviceRepository.countByStatus(DeviceStatus.MAINTENANCE);
        long total = deviceRepository.count();

        return new DashboardSummaryDto(running, stopped, maintenance, total);
    }
}
