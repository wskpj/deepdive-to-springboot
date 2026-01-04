package com.example.lib.security.starter.internal.config;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.lib.security.core.JwtProvider;
import com.example.lib.security.starter.internal.properties.SecurityProperties;
import com.example.lib.web.core.service.CookieManager;

class SecurityStarterConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SecurityStarterConfig.class))
            .withBean(CookieManager.class, () -> mock(CookieManager.class))
            .withPropertyValues(
                    "security.jwt-secret=defaulttestsecretkey123412341234123412341234",
                    "security.jwt-expiration-time=3600000",
                    "security.jwt-refresh-expiration-time=604800000"
            );

    @Test
    @DisplayName("SecurityFilterChain, PasswordEncoder, JwtProvider 빈이 등록되어야 한다")
    void shouldRegisterSecurityBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SecurityFilterChain.class);
            assertThat(context).hasSingleBean(PasswordEncoder.class);
            assertThat(context).hasSingleBean(JwtProvider.class);
        });
    }

    @Test
    @DisplayName("JwtProvider의 마스터 템플릿 기능(Access/Refresh Token 생성, 만료 토큰 파싱, 유효성 검증)이 정상 작동해야 한다")
    void shouldVerifyJwtProviderMasterFeatures() {
        contextRunner.run(context -> {
            @SuppressWarnings("unchecked")
            JwtProvider<Long> jwtProvider = context.getBean(JwtProvider.class);
            Long userId = 42L;
            String role = "ROLE_USER";

            // 1. Access Token & Refresh Token 생성 검증
            String accessToken = jwtProvider.createAccessToken(userId, role);
            String refreshToken = jwtProvider.createRefreshToken(userId);

            assertThat(accessToken).isNotBlank();
            assertThat(refreshToken).isNotBlank();

            // 2. 파싱 검증
            assertThat(jwtProvider.getUserId(accessToken)).isEqualTo(userId);
            assertThat(jwtProvider.getRole(accessToken)).isEqualTo(role);
            assertThat(jwtProvider.getUserId(refreshToken)).isEqualTo(userId);

            // 3. 유효성 검증
            assertThat(jwtProvider.validateToken(accessToken)).isTrue();
            var detail = jwtProvider.validateTokenDetail(accessToken);
            assertThat(detail.isValid()).isTrue();
            assertThat(detail.status()).isEqualTo(com.example.lib.security.core.JwtValidationResult.ValidationStatus.VALID);

            // 4. 만료 시간과 만료 토큰 파싱 검증을 위해 아주 짧은 만료 시간을 갖는 프로퍼티 컨텍스트 러너 구동
            new ApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(SecurityStarterConfig.class))
                    .withBean(CookieManager.class, () -> mock(CookieManager.class))
                    .withPropertyValues(
                            "security.jwt-secret=defaulttestsecretkey123412341234123412341234",
                            "security.jwt-expiration-time=1", // 1ms 만료
                            "security.jwt-refresh-expiration-time=1" // 1ms 만료
                    )
                    .run(shortContext -> {
                        @SuppressWarnings("unchecked")
                        JwtProvider<Long> shortProvider = shortContext.getBean(JwtProvider.class);
                        String expiredToken = shortProvider.createAccessToken(userId, role);

                        // 만료를 위한 지연
                        Thread.sleep(5);

                        // 일반 검증은 false여야 함
                        assertThat(shortProvider.validateToken(expiredToken)).isFalse();
                        
                        var expiredDetail = shortProvider.validateTokenDetail(expiredToken);
                        assertThat(expiredDetail.isValid()).isFalse();
                        assertThat(expiredDetail.status()).isEqualTo(com.example.lib.security.core.JwtValidationResult.ValidationStatus.EXPIRED);

                        // 만료되었지만 getUserIdFromExpiredToken은 정상적으로 ID를 반환해야 함 (Graceful Parsing)
                        assertThat(shortProvider.getUserIdFromExpiredToken(expiredToken)).isEqualTo(userId);
                    });
        });
    }
}
