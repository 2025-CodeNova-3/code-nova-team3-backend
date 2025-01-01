package com.team3.code_nova.backend.util.filter;

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
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Access token is missing or invalid\"}");
            return;
        }

        String accessToken = authorization.split(" ")[1];

        // 엑세스 토큰 검증
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Access token expired\"}");
            return;
        }

        // 토큰 타입 검증
        String accessTokenType = jwtUtil.getTokenType(accessToken);
        if (!accessTokenType.equals("access")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid access token\"}");
            return;
        }

        // 리프레시 토큰 획득
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String refreshTokenType = jwtUtil.getTokenType(refresh);
        if (!refreshTokenType.equals("refresh")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        if (!jwtUtil.isRefreshExist(refresh)) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        jwtUtil.deleteRefreshEntity(refresh);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}