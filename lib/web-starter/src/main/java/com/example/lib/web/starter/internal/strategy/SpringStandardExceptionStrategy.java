package com.example.lib.web.starter.internal.strategy;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 스프링 MVC의 표준 예외들(요청 파라미터/헤더/쿠키 누락, 타입 불일치, JSON 파싱 에러 등)이 발생했을 때
 * WARN 레벨로 로깅하여 클라이언트 측의 잘못된 요청을 추적하는 전략
 */
@Slf4j
@Order(10)
public class SpringStandardExceptionStrategy implements ExceptionHandleStrategy {

    public static final List<Class<? extends Exception>> TARGET_EXCEPTIONS = List.of(
            HttpMessageNotReadableException.class,         // JSON 파싱 에러
            MethodArgumentTypeMismatchException.class,     // 파라미터 타입 불일치
            MissingServletRequestParameterException.class, // 필수 쿼리 파라미터 누락
            MissingRequestHeaderException.class,          // 필수 요청 헤더(예: Authorization) 누락
            MissingRequestCookieException.class,          // 필수 쿠키 누락
            MissingPathVariableException.class,           // 경로 변수 누락
            HttpRequestMethodNotSupportedException.class,  // 잘못된 HTTP Method
            HttpMediaTypeNotSupportedException.class       // 지원하지 않는 Media Type
    );

    @Override
    public boolean supports(Exception e) {
        return TARGET_EXCEPTIONS.stream().anyMatch(type -> type.isInstance(e));
    }

    @Override
    public void handle(Exception e) {
    }
}
