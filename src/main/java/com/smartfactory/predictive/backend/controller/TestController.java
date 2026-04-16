package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.domain.entity.SensorReading;
import com.smartfactory.predictive.backend.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final DataSource dataSource;
    private final SensorReadingRepository sensorReadingRepository;

    // 접속 주소: http://localhost:8080/api/test/db
    @GetMapping("/db")
    public String testDb() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT NOW()")) { // DB한테 현재 시간 물어보기

            if (rs.next()) {
                return "✅ DB 연결 성공! Aiven 서버 시간: " + rs.getString(1);
            }
        } catch (Exception e) {
            return "❌ DB 연결 실패: " + e.getMessage();
        }
        return "알 수 없는 오류";
    }

    // 접속 주소: http://localhost:8080/api/test/sensors
    @GetMapping("/sensors")
    public List<SensorReading> getSensorDataTest() {
        // DB의 sensor_reading 테이블에 있는 데이터를 전부 가져와서 화면에 뿌려줍니다.
        return sensorReadingRepository.findAll();
    }
}