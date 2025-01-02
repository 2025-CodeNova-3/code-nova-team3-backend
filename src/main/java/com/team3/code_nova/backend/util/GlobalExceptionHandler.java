package com.team3.code_nova.backend.util;

import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomFilterException.class)
    public ResponseEntity<?> handleCustomFilterException(CustomFilterException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(
                new ApiResponse<>(ex.getStatusCode(), ex.getErrorCode(), ex.getMessage(), new EmptyResponse())
        );
    }
}