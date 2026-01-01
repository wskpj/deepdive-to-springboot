package com.example.lib.web.core.service;

import java.util.Optional;

/**
 * HTTP 쿠키의 추가, 삭제, 조회를 일관되게 처리하기 위한 매니저 인터페이스
 */
public interface CookieManager {

    void addCookie(String name, String value, int maxAge);

    void removeCookie(String name);

    Optional<String> getCookie(String name);
}
