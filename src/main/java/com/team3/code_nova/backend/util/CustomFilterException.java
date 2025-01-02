package com.team3.code_nova.backend.util;

import lombok.Getter;

@Getter
public class CustomFilterException extends RuntimeException {
    private final int statusCode;
    private final int errorCode;
    private final String message;

    public CustomFilterException(int statusCode, int errorCode, String message) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}