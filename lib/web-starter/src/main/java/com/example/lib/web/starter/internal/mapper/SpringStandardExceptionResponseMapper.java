package com.example.lib.web.starter.internal.mapper;

import com.example.lib.web.core.ApiError;
import com.example.lib.web.core.ExceptionResponseMapper;
import com.example.lib.web.core.WebError;
import com.example.lib.web.starter.internal.strategy.SpringStandardExceptionStrategy;

/**
 * 스프링 MVC의 표준 예외들을 400 Bad Request 로 변환하는 매퍼
 */
public class SpringStandardExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return SpringStandardExceptionStrategy.TARGET_EXCEPTIONS.stream().anyMatch(type -> type.isInstance(e));
    }

    @Override
    public ApiError map(Exception e, String uri) {
        return ApiError.of(WebError.BAD_REQUEST, uri);
    }
}
