package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.exception.WebException;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class WebExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return e instanceof WebException;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        WebException we = (WebException) e;
        return ApiError.of((WebError) we.getErrorType(), uri);
    }
}
