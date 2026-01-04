package com.example.lib.web.starter.internal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.example.lib.web.core.service.CookieOptions;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class StandardCookieManagerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StandardCookieManager cookieManager;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        cookieManager = new StandardCookieManager(request, response);
    }

    @Test
    @DisplayName("기본 addCookie 호출 시, 기본 보안 옵션(HttpOnly, Secure, Lax, Path=/)을 사용해야 한다")
    void addCookie_withDefaultOptions_setsSensibleDefaults() {
        // when
        cookieManager.addCookie("test-cookie", "test-val", 3600);

        // then
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq("Set-Cookie"), headerCaptor.capture());

        String headerValue = headerCaptor.getValue();
        assertThat(headerValue).contains("test-cookie=test-val");
        assertThat(headerValue).contains("Max-Age=3600");
        assertThat(headerValue).contains("Path=/");
        assertThat(headerValue).contains("HttpOnly");
        assertThat(headerValue).contains("Secure");
        assertThat(headerValue).contains("SameSite=Lax");
    }

    @Test
    @DisplayName("커스텀 CookieOptions를 사용하여 addCookie 호출 시, 설정된 대로 헤더를 빌드해야 한다")
    void addCookie_withCustomOptions_buildsCustomHeader() {
        // given
        CookieOptions options = CookieOptions.builder()
                .path("/api")
                .domain("example.com")
                .httpOnly(false)
                .secure(false)
                .sameSite("Strict")
                .build();

        // when
        cookieManager.addCookie("custom-cookie", "custom-val", 1800, options);

        // then
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq("Set-Cookie"), headerCaptor.capture());

        String headerValue = headerCaptor.getValue();
        assertThat(headerValue).contains("custom-cookie=custom-val");
        assertThat(headerValue).contains("Max-Age=1800");
        assertThat(headerValue).contains("Path=/api");
        assertThat(headerValue).contains("Domain=example.com");
        assertThat(headerValue).doesNotContain("HttpOnly");
        assertThat(headerValue).doesNotContain("Secure");
        assertThat(headerValue).contains("SameSite=Strict");
    }

    @Test
    @DisplayName("쿠키 조회 시, 해당 이름의 쿠키가 존재하면 값을 반환하고 없으면 Optional.empty()를 반환해야 한다")
    void getCookie_returnsOptionalValue() {
        // given
        Cookie cookie1 = new Cookie("theme", "dark");
        Cookie cookie2 = new Cookie("lang", "ko");
        given(request.getCookies()).willReturn(new Cookie[]{cookie1, cookie2});

        // when & then
        Optional<String> themeVal = cookieManager.getCookie("theme");
        Optional<String> langVal = cookieManager.getCookie("lang");
        Optional<String> invalidVal = cookieManager.getCookie("non-existent");

        assertThat(themeVal).hasValue("dark");
        assertThat(langVal).hasValue("ko");
        assertThat(invalidVal).isEmpty();
    }

    @Test
    @DisplayName("쿠키 배열이 null인 상황에서 getCookie 호출 시, Optional.empty()를 반환해야 한다")
    void getCookie_whenCookiesNull_returnsEmpty() {
        // given
        given(request.getCookies()).willReturn(null);

        // when
        Optional<String> val = cookieManager.getCookie("theme");

        // then
        assertThat(val).isEmpty();
    }

    @Test
    @DisplayName("기본 removeCookie 호출 시, Max-Age=0 및 기본 경로('/')로 만료 쿠키를 주입해야 한다")
    void removeCookie_withDefaultPath_expiresCookie() {
        // when
        cookieManager.removeCookie("theme");

        // then
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq("Set-Cookie"), headerCaptor.capture());

        String headerValue = headerCaptor.getValue();
        assertThat(headerValue).contains("theme=");
        assertThat(headerValue).contains("Max-Age=0");
        assertThat(headerValue).contains("Path=/");
    }

    @Test
    @DisplayName("커스텀 경로와 도메인 지정을 통한 removeCookie 호출 시, 해당 경로와 도메인의 쿠키를 만료시켜야 한다")
    void removeCookie_withCustomPathAndDomain_expiresTargetCookie() {
        // when
        cookieManager.removeCookie("session", "/api", "example.com");

        // then
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq("Set-Cookie"), headerCaptor.capture());

        String headerValue = headerCaptor.getValue();
        assertThat(headerValue).contains("session=");
        assertThat(headerValue).contains("Max-Age=0");
        assertThat(headerValue).contains("Path=/api");
        assertThat(headerValue).contains("Domain=example.com");
    }
}
