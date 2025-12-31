package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.HandledException;
import com.example.lib.web.core.ApiError;
import com.example.lib.web.core.ExceptionResponseMapper;
import com.example.lib.web.core.WebError;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
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
