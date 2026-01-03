package com.example.lib.security.starter.internal.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 보안 모듈(security-starter) 관련 프로퍼티 클래스
 */
@Validated
@ConfigurationProperties(prefix = "security")
public record SecurityProperties(

    @NotBlank
    String jwtSecret,

    @NotNull
    Long jwtExpirationTime,

    @NotNull
    Long jwtRefreshExpirationTime
) {}
