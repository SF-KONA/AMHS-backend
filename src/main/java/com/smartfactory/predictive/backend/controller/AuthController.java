package com.smartfactory.predictive.backend.controller;

import com.smartfactory.predictive.backend.dto.AuthResponseDto;
import com.smartfactory.predictive.backend.dto.LoginRequestDto;
import com.smartfactory.predictive.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
}