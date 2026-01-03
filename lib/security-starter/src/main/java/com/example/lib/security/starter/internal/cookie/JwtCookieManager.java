package com.example.lib.security.starter.internal.cookie;

import java.util.Optional;

import com.example.lib.security.starter.internal.properties.SecurityProperties;
import com.example.lib.web.core.service.CookieManager;

import lombok.RequiredArgsConstructor;

/**
 * JWT Refresh Token을 보안 쿠키(HttpOnly, Secure)로 관리하는 헬퍼 클래스
 */
@RequiredArgsConstructor
public class JwtCookieManager {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private final CookieManager cookieManager;
    private final SecurityProperties securityProperties;

    /**
     * Refresh Token을 HTTP-Only, Secure 쿠키로 저장합니다.
     *
     * @param refreshToken 저장할 리프레시 토큰
     */
    public void setRefreshTokenCookie(String refreshToken) {
        if (refreshToken == null) {
            return;
        }
        int maxAgeInSeconds = (int) (securityProperties.jwtRefreshExpirationTime() / 1000);
        cookieManager.addCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, maxAgeInSeconds);
    }

    /**
     * HTTP 요청의 쿠키 목록에서 Refresh Token을 추출합니다.
     *
     * @return 추출된 리프레시 토큰 (존재하지 않으면 Optional.empty())
     */
    public Optional<String> getRefreshTokenCookie() {
        return cookieManager.getCookie(REFRESH_TOKEN_COOKIE_NAME);
    }

    /**
     * Refresh Token 쿠키를 만료(삭제) 처리합니다.
     */
    public void removeRefreshTokenCookie() {
        cookieManager.removeCookie(REFRESH_TOKEN_COOKIE_NAME);
    }
}
