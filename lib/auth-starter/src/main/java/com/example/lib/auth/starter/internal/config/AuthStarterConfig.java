package com.example.lib.auth.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import com.example.lib.auth.starter.internal.api.StandardAuthController;
import com.example.lib.auth.starter.internal.port.AuthPort;
import com.example.lib.security.core.JwtProvider;
import com.example.lib.security.starter.internal.cookie.JwtCookieManager;

/**
 * 회원/인증 모듈(auth-starter)의 자동 구성(AutoConfiguration) 클래스.
 * 헥사고날 아키텍처 원칙에 따라, 애플리케이션 영역에서 포트의 실구현체(AuthPort)를 Bean으로 등록한 경우에만
 * 공통 인바운드 웹 어댑터인 AuthController를 스프링 컨텍스트에 자동 마운트합니다.
 */
@AutoConfiguration
public class AuthStarterConfig {

    /**
     * AuthPort 구현체가 존재할 경우에 한하여 공통 인증 컨트롤러 빈을 등록합니다.
     *
     * @param authPort         도메인 레이어에서 구현한 실질적 비즈니스 처리용 아웃바운드 포트
     * @param jwtCookieManager HTTP-Only 쿠키 관리기
     * @param jwtProvider      공통 JWT 암복호화/검증기
     * @return 자동 마운트될 공통 인증 컨트롤러
     */
    @Bean
    @ConditionalOnBean(AuthPort.class)
    public StandardAuthController standardAuthController(
            AuthPort authPort,
            JwtCookieManager jwtCookieManager,
            JwtProvider<Long> jwtProvider) {
        return new StandardAuthController(authPort, jwtCookieManager, jwtProvider);
    }
}
