package com.example.lib.web.starter.internal.service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

import com.example.lib.web.core.service.CookieManager;
import com.example.lib.web.core.service.CookieOptions;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * HTTP 서블릿 요청/응답 객체를 활용하여 쿠키를 제어하는 기본 CookieManager 구현체
 * 기본 보안 옵션 및 사용자 정의 상세 설정을 완벽히 처리하는 마스터 템플릿입니다.
 */
@RequiredArgsConstructor
@SuppressWarnings("null")
public class StandardCookieManager implements CookieManager {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public void addCookie(String name, String value, int maxAge) {
        addCookie(name, value, maxAge, CookieOptions.defaultOptions());
    }

    @Override
    public void addCookie(String name, String value, int maxAge, CookieOptions options) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .path(options.getPath())
                .maxAge(maxAge)
                .httpOnly(options.isHttpOnly())
                .secure(options.isSecure())
                .sameSite(options.getSameSite());

        if (options.getDomain() != null) {
            builder.domain(options.getDomain());
        }

        response.addHeader("Set-Cookie", builder.build().toString());
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
        removeCookie(name, "/", null);
    }

    @Override
    public void removeCookie(String name, String path, String domain) {
        CookieOptions options = CookieOptions.builder()
                .path(path)
                .domain(domain)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();
        addCookie(name, "", 0, options);
    }
}
