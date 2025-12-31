package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

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
