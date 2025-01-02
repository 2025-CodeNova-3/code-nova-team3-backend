package com.team3.code_nova.backend.util.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.code_nova.backend.dto.auth.CustomUserDetails;
import com.team3.code_nova.backend.util.CustomFilterException;
import com.team3.code_nova.backend.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = new ObjectMapper();
        this.jwtUtil = jwtUtil;
        this.setFilterProcessesUrl("/api/auth/sign-in");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username;
        String password;

        // JSON 형식 요청 받아오기
        try {
            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String> jsonRequest = objectMapper.readValue(body, Map.class);

            username = jsonRequest.get("username");
            password = jsonRequest.get("password");
        } catch (IOException e) {

            throw new CustomFilterException(400, 2001, "잘못된 요청 JSON 형식");
        }

        // 스프링 시큐리티에서 username과 password를 검증하기 위해 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공 시 실행하는 메소드 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtil.generateAccessToken(userId, username, role);
        String refreshToken = jwtUtil.generateRefreshToken(userId, username, role);

        // 응답 설정
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");

        Map<String, Object> jsonResponse = Map.of(
                "status", 200,
                "code", 2000,
                "message", "로그인 성공! 엑세스, 리프레시 토큰 발급 완료",
                "data", Map.of(
                        "access", accessToken,
                        "refresh", refreshToken
                )
        );

        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
    }

    // 로그인 실패 시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        String errorMessage = failed.getCause() instanceof UsernameNotFoundException
                ? "username에 해당하는 User 미존재"
                : "비밀번호 불일치";

        Map<String, Object> jsonResponse = Map.of(
                "status", 401,
                "code", 2002,
                "message", errorMessage,
                "data", Map.of(
                        "message", "No data available"
                )
        );

        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
    }
}
