package com.team3.code_nova.backend.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "API 공통 응답 형식")
public class BasicApiResponse<T> {
    @Schema(description = "상태 코드", example = "200")
    private int status;

    @Schema(description = "응답 코드", example = "0")
    private int code;

    @Schema(description = "응답 메시지", example = "요청 처리 완료")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while converting ApiResponse to JSON", e);
        }
    }
}