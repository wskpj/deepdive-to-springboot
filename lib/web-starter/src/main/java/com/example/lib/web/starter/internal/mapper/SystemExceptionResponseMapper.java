package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.SystemException;
import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

/**
 * 처리되지 않은 시스템 내부 예외(SystemException)를 
 * 서버 오류인 INTERNAL_SERVER_ERROR(500) 응답 객체로 변환하는 매퍼
 */
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class SystemExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemException;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        return ApiError.of(WebError.INTERNAL_SERVER_ERROR, uri);
    }
}
