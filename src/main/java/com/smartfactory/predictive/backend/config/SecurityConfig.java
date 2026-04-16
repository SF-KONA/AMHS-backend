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
                        // 1. 로그인 API 허용
                        .requestMatchers("/api/auth/login").permitAll()

                        // 2. 장비 목록 및 모든 서브 경로(상세, 센서 등) 허용
                        .requestMatchers("/api/equipments/**").permitAll()

                        // 3. 나머지는 여전히 인증 필요
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}