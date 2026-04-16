package com.smartfactory.predictive.backend.scheduler;

import com.smartfactory.predictive.backend.dto.SensorDataResponseDto;
import com.smartfactory.predictive.backend.repository.DeviceRepository;
import com.smartfactory.predictive.backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SensorPushScheduler {

    private final EquipmentService equipmentService;
    private final SimpMessagingTemplate messagingTemplate;
    private final DeviceRepository deviceRepository;

    private List<String> deviceIds;

    // 서버 시작할 때 DB에서 장비 목록 한 번만 로드
    @PostConstruct
    public void init() {
        deviceIds = deviceRepository.findAll()
                .stream()
                .map(d -> d.getDeviceId())
                .toList();
    }

    @Scheduled(fixedRate = 1500)
    public void pushRealTimeSensorData() {
        for (String deviceId : deviceIds) {
            equipmentService.getLatestSensorData(deviceId).ifPresent(data ->
                    messagingTemplate.convertAndSend("/topic/sensors/" + deviceId, data)
            );
        }
    }
}