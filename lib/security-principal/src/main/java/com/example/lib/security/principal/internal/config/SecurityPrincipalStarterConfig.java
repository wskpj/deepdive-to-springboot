package com.example.lib.security.principal.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.example.lib.common.core.context.PrincipalContext;
import com.example.lib.security.principal.internal.context.SpringSecurityPrincipalContext;

/**
 * 토큰 기반 인증 주체(Principal) 설정을 자동화하는 모듈 구성
 */
@AutoConfiguration
@EnableMethodSecurity // @PreAuthorize 등의 AOP 기반 인가 활성화
public class SecurityPrincipalStarterConfig {

    @Bean
    @ConditionalOnMissingBean(PrincipalContext.class)
    public PrincipalContext<Long> principalContext() {
        return new SpringSecurityPrincipalContext<>(Long.class);
    }
}
