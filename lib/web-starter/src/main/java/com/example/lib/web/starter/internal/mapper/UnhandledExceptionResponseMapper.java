package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.web.core.ApiError;
import com.example.lib.web.core.ExceptionResponseMapper;
import com.example.lib.web.core.WebError;

@Order(Ordered.LOWEST_PRECEDENCE)
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
