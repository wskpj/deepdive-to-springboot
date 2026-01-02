package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.annotation.Order;

import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

/**
 * 어떤 매퍼도 지원하지 않는 최후의 일반 예외(Exception)를 
 * 서버 오류인 INTERNAL_SERVER_ERROR(500) 응답 객체로 변환하는 폴백(Fallback) 매퍼
 */
@Order(1)
public class UnhandledExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return true;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        return ApiError.of(WebError.INTERNAL_SERVER_ERROR, uri);
    }
}
