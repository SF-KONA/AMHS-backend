package com.smartfactory.predictive.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. 로그인 및 Swagger 관련 경로 모두 허용
                        .requestMatchers(
                                "/api/auth/login",
                                "/v3/api-docs/**",    // Swagger 데이터 경로
                                "/swagger-ui/**",     // Swagger UI 경로
                                "/swagger-ui.html"    // Swagger HTML 접속 경로
                        ).permitAll()

                        // 2. 장비 및 센서 API 허용
                        .requestMatchers("/api/equipments/**").permitAll()

                        // 3. 나머지는 인증 필요
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}