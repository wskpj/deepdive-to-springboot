package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.annotation.Order;

import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;
import com.example.lib.web.starter.internal.strategy.SpringStandardExceptionStrategy;

/**
 * 스프링 MVC의 표준 예외들(파라미터 누락, 타입 불일치 등)을 
 * 클라이언트 오류인 BAD_REQUEST(400) 응답 객체로 변환하는 매퍼
 */
@Order(10)
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
