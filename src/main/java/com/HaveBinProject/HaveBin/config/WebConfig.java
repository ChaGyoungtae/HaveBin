package com.HaveBinProject.HaveBin.config;

import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    /**
     * 스프링부트 5.3 이후,
     * allowCredentials가 true일 때, allowedOrigins에 특수 값인 "*" 추가할 수 없게 되었다.
     * 대신 allowOriginPatterns를 사용해야 한다.
     */

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000", "http://43.200.164.14") // React & EC2 주소 허용
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키, 인증 정보 허용
                .exposedHeaders(HttpHeaders.LOCATION);
    }
}
