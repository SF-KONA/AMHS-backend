package com.smartfactory.predictive.backend.scheduler;

import com.smartfactory.predictive.backend.dto.SensorDataResponseDto;
import com.smartfactory.predictive.backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SensorPushScheduler {

    private final EquipmentService equipmentService;
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전송 도구

    // 1.5초(1500ms)마다 실행
    @Scheduled(fixedRate = 1500)
    public void pushRealTimeSensorData() {
        // 테스트를 위해 특정 장비(oht02)의 최신 1건만 가져옵니다.
        List<SensorDataResponseDto> latest = equipmentService.getRecentSensorData("oht02");

        if (!latest.isEmpty()) {
            // 가장 최신 데이터(0번 인덱스)를 특정 채널로 전송!
            messagingTemplate.convertAndSend("/topic/sensors/oht02", latest.get(0));
        }
    }
}
