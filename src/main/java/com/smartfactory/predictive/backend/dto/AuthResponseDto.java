package com.smartfactory.predictive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto {
    private boolean success;
    private String message;
    private String token;
}