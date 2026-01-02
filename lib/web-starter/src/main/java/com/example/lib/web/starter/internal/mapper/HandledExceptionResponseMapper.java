package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.HandledException;
import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

/**
 * 비즈니스 로직에서 의도적으로 발생시킨 HandledException을 
 * 클라이언트에게 반환할 ApiError(BAD_REQUEST 등)로 변환하는 매퍼
 */
@Order(10)
public class HandledExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return e instanceof HandledException;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        return ApiError.of(WebError.BAD_REQUEST, uri);
    }
}
