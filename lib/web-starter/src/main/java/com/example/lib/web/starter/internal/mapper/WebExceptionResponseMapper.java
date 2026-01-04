package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.annotation.Order;

import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.exception.WebException;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

/**
 * 웹 계층에서 명시적으로 발생시킨 WebException을 
 * 예외가 품고 있는 WebError 상태에 맞춰 해당 ApiError로 변환하는 매퍼
 */
@Order(1)
public class WebExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return e instanceof WebException;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        WebException we = (WebException) e;
        WebError error = (WebError) we.getErrorType();
        String message = we.getMessage() != null ? we.getMessage() : error.getMessage();
        return new ApiError(
            error.getStatus(),
            error.getCode(),
            message,
            uri,
            new java.util.HashMap<>()
        );
    }
}
