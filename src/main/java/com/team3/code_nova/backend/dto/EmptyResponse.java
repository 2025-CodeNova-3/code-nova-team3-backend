package com.team3.code_nova.backend.dto;

import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public class EmptyResponse {

    @Schema(description = "응답 메시지", example = "No data available")
    private final String message;

    public EmptyResponse() {
        this.message = "No data available";
    }

    // 선택적: 사용자 정의 메시지를 허용하는 생성자
    public EmptyResponse(String message) {
        this.message = message;
    }
}
