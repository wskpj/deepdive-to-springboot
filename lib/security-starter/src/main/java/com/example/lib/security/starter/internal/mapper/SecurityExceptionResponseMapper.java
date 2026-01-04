package com.example.lib.security.starter.internal.mapper;

import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

/**
 * Security 모듈 전용 예외 매퍼.
 * 스프링 시큐리티 예외를 403 Forbidden 또는 401 Unauthorized 응답으로 변환합니다.
 */
@Order(100)
public class SecurityExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return e instanceof AccessDeniedException || e instanceof AuthenticationException;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        if (e instanceof AccessDeniedException) {
            return ApiError.of(WebError.FORBIDDEN, uri);
        }
        if (e instanceof AuthenticationException) {
            return ApiError.of(WebError.UNAUTHORIZED, uri);
        }
        return ApiError.of(WebError.INTERNAL_SERVER_ERROR, uri);
    }
}
