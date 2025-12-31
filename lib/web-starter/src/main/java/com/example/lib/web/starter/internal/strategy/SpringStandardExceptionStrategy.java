package com.example.lib.web.starter.internal.strategy;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 스프링 MVC 의 표준 예외들을 처리하는 전략
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 10)
@RequiredArgsConstructor
public class SpringStandardExceptionStrategy implements ExceptionHandleStrategy {

    private final ExceptionContextTracer tracer;

    public static final List<Class<? extends Exception>> TARGET_EXCEPTIONS = List.of(
            HttpMessageNotReadableException.class,         // JSON 파싱 에러
            MethodArgumentTypeMismatchException.class,     // 파라미터 타입 불일치
            MissingServletRequestParameterException.class, // 필수 파라미터 누락
            HttpRequestMethodNotSupportedException.class,  // 잘못된 HTTP Method
            HttpMediaTypeNotSupportedException.class       // 지원하지 않는 Media Type
    );

    @Override
    public boolean supports(Exception e) {
        return TARGET_EXCEPTIONS.stream().anyMatch(type -> type.isInstance(e));
    }

    @Override
    public void handle(Exception e) {
        log.warn("[Handled Spring Exception] {}", e.getClass().getSimpleName());
        tracer.handle(e);
    }
}
