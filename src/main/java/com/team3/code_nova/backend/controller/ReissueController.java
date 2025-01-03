package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.AccessAndRefreshTokenDTO;
import com.team3.code_nova.backend.dto.BasicApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@Tag(name = "Reissue API", description = "RefeshToken 재발급 API를 관리합니다.")
public class ReissueController {
    private final JWTUtil jwtUtil;

    public ReissueController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/auth/reissue")
    @Operation(summary = "리프레시 토큰 재발급", description = "리프레시 토큰을 사용하여 엑세스 토큰과 새로운 리프레시 토큰을 재발급합니다.")
    public ResponseEntity<?> reissue(
            @Parameter(description = "HTTP 요청 객체") HttpServletRequest request,
            @Parameter(description = "HTTP 응답 객체") HttpServletResponse response) {

        // Refresh Token 추출
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(400).body(
                    new BasicApiResponse<>(400, 4001, "쿠키 값 미설정", new EmptyResponse())
            );
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return ResponseEntity.status(400).body(
                    new BasicApiResponse<>(400, 4002, "쿠키에 refresh 값 미존재", new EmptyResponse())
            );
        }

        // 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(400).body(
                    new BasicApiResponse<>(400, 4003, "리프레시 토큰 만료", new EmptyResponse())
            );
        }

        // 토큰 타입 체크 (리프레시 토큰인지 확인)
        String tokenType = jwtUtil.getTokenType(refresh);
        if (!tokenType.equals("refresh")) {
            return ResponseEntity.status(400).body(
                    new BasicApiResponse<>(400, 4004, "토큰 타입 미일치", new EmptyResponse())
            );
        }

        // DB에 리프레시 토큰 존재 여부 확인
        if (!jwtUtil.isRefreshExist(refresh)) {
            return ResponseEntity.status(400).body(
                    new BasicApiResponse<>(400, 4005, "미등록 리프레시 토큰", new EmptyResponse())
            );
        }

        Long userId = jwtUtil.getUserId(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // 새로운 엑세스 토큰과 리프레시 토큰 생성
        String newAccessToken = jwtUtil.generateAccessToken(userId, username, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, username, role);

        // 기존 리프레시 토큰 엔티티 삭제
        jwtUtil.deleteRefreshEntity(refresh);

        // 토큰 DTO 생성
        AccessAndRefreshTokenDTO accessAndRefreshTokenDTO = new AccessAndRefreshTokenDTO(newAccessToken, newRefreshToken);

        return ResponseEntity.status(200).body(
                new BasicApiResponse<>(200, 4000, "엑세스, 리프레시 토큰 재발급 완료", accessAndRefreshTokenDTO)
        );
    }
}
