package com.team3.code_nova.backend.dto;

import lombok.Getter;

@Getter
public class EmptyResponse {
    private final String message;

    public EmptyResponse() {
        this.message = "No data available";
    }

    // 선택적: 사용자 정의 메시지를 허용하는 생성자
    public EmptyResponse(String message) {
        this.message = message;
    }
}