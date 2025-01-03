package com.team3.code_nova.backend.config;

import com.team3.code_nova.backend.dto.EmptyResponse;
import com.team3.code_nova.backend.util.InnerFilterResponse;
import com.team3.code_nova.backend.util.JWTUtil;
import com.team3.code_nova.backend.util.filter.CustomLogoutFilter;
import com.team3.code_nova.backend.util.filter.JWTFilter;
import com.team3.code_nova.backend.util.filter.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.team3.code_nova.backend.enums.Role.ADMIN;
import static com.team3.code_nova.backend.enums.Role.USER;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.front.domain}")
    private String frontDomain;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Role Hierarchy 필요하다면 추가

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        //CORS 설정
        httpSecurity
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", frontDomain));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //csrf disable
        httpSecurity
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        httpSecurity
                .formLogin((auth) -> auth.disable());


        //HTTP Basic 인증 방식 disable
        httpSecurity
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/auth/sign-up", "/api/auth/sign-in").permitAll()
                        .requestMatchers("/api/auth/reissue").permitAll()
                        .requestMatchers("/api/auth/sign-out").hasAnyAuthority(ADMIN.getValue(), USER.getValue())
                        .requestMatchers("/api/users/**").hasAnyAuthority(ADMIN.getValue(), USER.getValue())
                        .requestMatchers("/api/boards/**").hasAnyAuthority(ADMIN.getValue(), USER.getValue())
                        .anyRequest().authenticated())
                
                // 인증 실패 시 로그인 페이지로 리다이렉트가 아닌 401 응답 뱉도록 설정
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint((request, response, authException) -> {

                    InnerFilterResponse.sendInnerResponse(response, 500, 500,
                            "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", new EmptyResponse());
                }));

        //JWTFilter 추가
        httpSecurity
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //LoginFilter 추가
        httpSecurity
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //CustomLogoutFilter 추가
        httpSecurity
                .addFilterAfter(new CustomLogoutFilter(jwtUtil), JWTFilter.class);

        //세션 설정 : STATELESS
        httpSecurity
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
