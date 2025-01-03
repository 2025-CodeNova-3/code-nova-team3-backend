package com.team3.code_nova.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan
//@EnableWebMvc
public class AutoWebConfig implements WebMvcConfigurer {

    @Value("${spring.front.domain}")
    private String frontDomain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // cors를 적용할 spring서버의 url 패턴.
                .allowedOrigins("http://localhost:3000", frontDomain)
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .exposedHeaders("Set-Cookie", "Authorization");
    }
}

