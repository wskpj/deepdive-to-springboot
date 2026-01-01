package com.example.lib.web.starter.internal.service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

import com.example.lib.web.core.service.CookieManager;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * HTTP 서블릿 요청/응답 객체를 활용하여 쿠키를 제어하는 기본 CookieManager 구현체
 * 보안 설정(HttpOnly, Secure, SameSite)이 적용된 쿠키를 생성합니다.
 */
@RequiredArgsConstructor
@SuppressWarnings("null")
public class StandardCookieManager implements CookieManager {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public void addCookie(String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    @Override
    public Optional<String> getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.ofNullable(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void removeCookie(String name) {
        addCookie(name, "", 0);
    }
}
