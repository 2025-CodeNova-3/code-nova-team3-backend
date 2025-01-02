package com.team3.code_nova.backend.util.filter;

import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.util.InnerFilterResponse;
import com.team3.code_nova.backend.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

// 프론트엔드측 : 로컬 스토리지에 존재하는 Access 토큰 삭제 및 서버측 로그아웃 경로로 Refresh 토큰 전송
// 추가 가능 로직 : 모든 계정에서 로그아웃 구현시 username 기반으로 모든 Refresh 토큰 삭제도 가능 - 우리 서비스엔 맞지 않음
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;

    public CustomLogoutFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();

        if (!requestUri.matches("^/api/auth/sign-out$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 엑세스 토큰 검증 추가
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더가 없는 경우
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3001,
                    "Authorization 헤더 미설정", new EmptyResponse());
            return;
        }

        String accessToken = authorization.split(" ")[1];

        // 엑세스 토큰 검증
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3002,
                    "엑세스 토큰 만료", new EmptyResponse());
            return;
        }

        // 토큰 타입 검증
        String accessTokenType = jwtUtil.getTokenType(accessToken);
        if (!accessTokenType.equals("access")) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3003,
                    "엑세스 토큰 타입 미일치", new EmptyResponse());
            return;
        }

        // 리프레시 토큰 획득
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3004,
                    "쿠키 자체에 값이 미존재", new EmptyResponse());
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                break;
            }
        }

        //refresh null check
        if (refresh == null) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3005,
                    "쿠키에 refresh 값 미존재", new EmptyResponse());
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3006,
                    "리프레시 토큰 만료", new EmptyResponse());
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String refreshTokenType = jwtUtil.getTokenType(refresh);
        if (!refreshTokenType.equals("refresh")) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3007,
                    "리프레시 토큰 타입 미일치", new EmptyResponse());
            return;
        }

        //DB에 저장되어 있는지 확인
        if (!jwtUtil.isRefreshExist(refresh)) {

            InnerFilterResponse.sendInnerResponse(response, 400, 3008,
                    "미등록 리프레시 토큰", new EmptyResponse());
            return;
        }

        //Refresh 토큰 DB에서 제거
        jwtUtil.deleteRefreshEntity(refresh);

        InnerFilterResponse.sendInnerResponse(response, 200, 3000,
                "로그아웃 성공", new EmptyResponse());
    }
}