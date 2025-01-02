package com.team3.code_nova.backend.util;

import com.team3.code_nova.backend.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class InnerFilterResponse {

    public static <T> void sendInnerResponse(HttpServletResponse response, int status, int code, String message, T data) throws IOException {
        ApiResponse<T> apiResponse = new ApiResponse<>(status, code, message, data);
        sendResponse(response, apiResponse);
    }

    // 실제 응답을 전송하는 메소드
    private static void sendResponse(HttpServletResponse response, ApiResponse<?> apiResponse) throws IOException {
        response.setStatus(apiResponse.getStatus());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(apiResponse.toString());
    }
}
