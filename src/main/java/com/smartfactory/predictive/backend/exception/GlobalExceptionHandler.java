package com.smartfactory.predictive.backend.exception;

import com.smartfactory.predictive.backend.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 여기서 다 잡습니다!
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class) // 우리가 서비스에서 던진 RuntimeException 처리
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class) // 그 외 예상치 못한 모든 에러 처리
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        ErrorResponse response = new ErrorResponse("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}