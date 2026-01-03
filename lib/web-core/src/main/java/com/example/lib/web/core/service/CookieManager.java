package com.example.lib.web.core.service;

import java.util.Optional;

/**
 * HTTP 쿠키의 추가, 삭제, 조회를 일관되게 처리하기 위한 매니저 인터페이스
 */
public interface CookieManager {

    /**
     * Sensible default 옵션(Path: /, HttpOnly: true, Secure: true, SameSite: Lax)으로 쿠키를 추가합니다.
     */
    void addCookie(String name, String value, int maxAge);

    /**
     * 세부 옵션(CookieOptions)을 커스텀 정의하여 쿠키를 추가합니다.
     */
    void addCookie(String name, String value, int maxAge, CookieOptions options);

    /**
     * 특정 이름의 쿠키를 안전하게 조회합니다.
     */
    Optional<String> getCookie(String name);

    /**
     * 기본 경로('/')의 쿠키를 제거합니다.
     */
    void removeCookie(String name);

    /**
     * 커스텀 경로 및 도메인의 쿠키를 제거합니다.
     */
    void removeCookie(String name, String path, String domain);
}
