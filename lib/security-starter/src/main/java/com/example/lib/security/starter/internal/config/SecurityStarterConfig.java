package com.example.lib.security.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.lib.security.core.JwtProvider;
import com.example.lib.security.starter.internal.cookie.JwtCookieManager;
import com.example.lib.security.starter.internal.filter.JwtAuthenticationFilter;
import com.example.lib.security.starter.internal.mapper.SecurityExceptionResponseMapper;
import com.example.lib.security.starter.internal.properties.SecurityProperties;
import com.example.lib.security.starter.internal.provider.DefaultJwtProvider;
import com.example.lib.web.core.service.CookieManager;

/**
 * 보안 모듈(security-starter)의 자동 구성(AutoConfiguration) 클래스
 * 기본 보안 필터 체인(SecurityFilterChain) 및 비밀번호 암호화(PasswordEncoder) 빈을 등록합니다.
 */
@AutoConfiguration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityStarterConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, 
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationFilter.class)
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider<Long> jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(JwtProvider.class)
    public JwtProvider<Long> jwtProvider(SecurityProperties securityProperties) {
        return new DefaultJwtProvider<>(securityProperties, Long::valueOf);
    }

    @Bean
    @ConditionalOnMissingBean(JwtCookieManager.class)
    public JwtCookieManager jwtCookieManager(
            CookieManager cookieManager,
            SecurityProperties securityProperties) {
        return new JwtCookieManager(cookieManager, securityProperties);
    }

    @Bean
    public SecurityExceptionResponseMapper securityExceptionResponseMapper() {
        return new SecurityExceptionResponseMapper();
    }
}
