package com.team3.code_nova.backend.controller;

import com.team3.code_nova.backend.dto.AccessAndRefreshTokenDTO;
import com.team3.code_nova.backend.dto.ApiResponse;
import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {
    private final JWTUtil jwtUtil;

    public ReissueController(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/auth/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if(cookies == null){

            return ResponseEntity.status(400).body(
                    new ApiResponse<>(400, 4001,"쿠키 값 미설정", new EmptyResponse())
            );
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            return ResponseEntity.status(400).body(
                    new ApiResponse<>(400, 4002,"쿠키에 refresh 값 미존재", new EmptyResponse())
            );
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            return ResponseEntity.status(400).body(
                    new ApiResponse<>(400, 4003,"리프레시 토큰 만료", new EmptyResponse())
            );
        }

        // 토큰이 refresh인지 확인 (발급 시 페이로드에 명시)
        String tokenType = jwtUtil.getTokenType(refresh);

        if (!tokenType.equals("refresh")) {
            //response status code
            return ResponseEntity.status(400).body(
                    new ApiResponse<>(400, 4004,"토큰 타입 미일치", new EmptyResponse())
            );
        }

        //DB에 저장되어 있는지 확인
        if (!jwtUtil.isRefreshExist(refresh)) {

            return ResponseEntity.status(400).body(
                    new ApiResponse<>(400, 4005, "미등록 리프레시 토큰", new EmptyResponse())
            );
        }

        Long userId = jwtUtil.getUserId(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccessToken = jwtUtil.generateAccessToken(userId, username, role);;
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, username, role);

        jwtUtil.deleteRefreshEntity(refresh);

        AccessAndRefreshTokenDTO accessAndRefreshTokenDTO = new AccessAndRefreshTokenDTO(newAccessToken, newRefreshToken);

        return ResponseEntity.status(200).body(
                new ApiResponse<>(200, 4000, "엑세스, 리프레시 토큰 재발급 완료", accessAndRefreshTokenDTO)
        );
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);   // HTTPS 에서만 쿠키를 사용할 수 있도록 설정
        cookie.setPath("/");        // 쿠키가 보일 위치 설정
        cookie.setHttpOnly(true);   // JavaScript 쿠키 조작 불가능

        return cookie;
    }
}
