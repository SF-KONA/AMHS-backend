package com.smartfactory.predictive.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private DataSource dataSource; // DB 연결 도구

    @GetMapping
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
}