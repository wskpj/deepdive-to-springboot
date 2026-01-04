package com.example.lib.security.starter.internal.cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.lib.security.starter.internal.properties.SecurityProperties;
import com.example.lib.web.core.service.CookieManager;

class JwtCookieManagerTest {

    private CookieManager cookieManager;
    private SecurityProperties securityProperties;
    private JwtCookieManager jwtCookieManager;

    @BeforeEach
    void setUp() {
        cookieManager = mock(CookieManager.class);
        securityProperties = mock(SecurityProperties.class);
        jwtCookieManager = new JwtCookieManager(cookieManager, securityProperties);
    }

    @Test
    @DisplayName("setRefreshTokenCookie - 리프레시 토큰이 정상적으로 주어지면 쿠키를 초 단위 만료 수명으로 환산하여 저장한다")
    void setRefreshTokenCookie_success() {
        // given
        String refreshToken = "sample-refresh-token";
        long expirationTimeMs = 604800000L; // 7일 (ms)
        int expectedMaxAgeSeconds = 604800; // 7일 (s)

        when(securityProperties.jwtRefreshExpirationTime()).thenReturn(expirationTimeMs);

        // when
        jwtCookieManager.setRefreshTokenCookie(refreshToken);

        // then
        verify(securityProperties).jwtRefreshExpirationTime();
        
        org.mockito.ArgumentCaptor<com.example.lib.web.core.service.CookieOptions> optionsCaptor = 
                org.mockito.ArgumentCaptor.forClass(com.example.lib.web.core.service.CookieOptions.class);
        verify(cookieManager).addCookie(
                eq("refresh_token"), 
                eq(refreshToken), 
                eq(expectedMaxAgeSeconds), 
                optionsCaptor.capture()
        );

        com.example.lib.web.core.service.CookieOptions capturedOptions = optionsCaptor.getValue();
        assertEquals("/", capturedOptions.getPath());
        assertTrue(capturedOptions.isHttpOnly());
        assertTrue(capturedOptions.isSecure());
        assertEquals("Strict", capturedOptions.getSameSite());
    }

    @Test
    @DisplayName("setRefreshTokenCookie - 리프레시 토큰이 null이면 아무런 작업도 수행하지 않는다")
    void setRefreshTokenCookie_nullToken() {
        // when
        jwtCookieManager.setRefreshTokenCookie(null);

        // then
        verifyNoInteractions(cookieManager, securityProperties);
    }

    @Test
    @DisplayName("getRefreshTokenCookie - 리프레시 토큰 쿠키가 존재하면 정상적으로 반환한다")
    void getRefreshTokenCookie_exists() {
        // given
        String refreshToken = "sample-refresh-token";
        when(cookieManager.getCookie("refresh_token")).thenReturn(Optional.of(refreshToken));

        // when
        Optional<String> result = jwtCookieManager.getRefreshTokenCookie();

        // then
        assertTrue(result.isPresent());
        assertEquals(refreshToken, result.get());
        verify(cookieManager).getCookie("refresh_token");
    }

    @Test
    @DisplayName("getRefreshTokenCookie - 리프레시 토큰 쿠키가 존재하지 않으면 빈 Optional을 반환한다")
    void getRefreshTokenCookie_notExists() {
        // given
        when(cookieManager.getCookie("refresh_token")).thenReturn(Optional.empty());

        // when
        Optional<String> result = jwtCookieManager.getRefreshTokenCookie();

        // then
        assertFalse(result.isPresent());
        verify(cookieManager).getCookie("refresh_token");
    }

    @Test
    @DisplayName("removeRefreshTokenCookie - 리프레시 토큰 쿠키 삭제 메서드를 정상 호출한다")
    void removeRefreshTokenCookie_success() {
        // when
        jwtCookieManager.removeRefreshTokenCookie();

        // then
        verify(cookieManager).removeCookie("refresh_token");
    }
}
