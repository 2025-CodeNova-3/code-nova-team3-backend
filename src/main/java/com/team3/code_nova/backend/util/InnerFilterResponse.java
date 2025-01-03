package com.team3.code_nova.backend.util;

import com.team3.code_nova.backend.dto.BasicApiResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class InnerFilterResponse {

    public static <T> void sendInnerResponse(HttpServletResponse response, int status, int code, String message, T data) throws IOException {
        BasicApiResponse<T> basicApiResponse = new BasicApiResponse<>(status, code, message, data);
        sendResponse(response, basicApiResponse);
    }

    // 실제 응답을 전송하는 메소드
    private static void sendResponse(HttpServletResponse response, BasicApiResponse<?> basicApiResponse) throws IOException {
        response.setStatus(basicApiResponse.getStatus());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(basicApiResponse.toString());
    }
}
