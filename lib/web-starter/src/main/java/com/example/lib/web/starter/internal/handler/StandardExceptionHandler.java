package com.example.lib.web.starter.internal.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.web.core.dispatcher.ExceptionResponseDispatcher;
import com.example.lib.web.core.dto.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 스프링 MVC 컨트롤러 계층에서 발생하는 모든 예외를 전역적으로 가로채는 핸들러(ControllerAdvice)
 * 발생한 예외의 내부 처리(HandleDispatcher)와 외부 응답 변환(ResponseDispatcher)을 조율
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class StandardExceptionHandler {

    private final ExceptionHandleDispatcher handler;
    private final ExceptionResponseDispatcher mapper;

    @ExceptionHandler(Exception.class)
    public ApiError handleAllException(Exception e, HttpServletRequest request) {
        // 1. 예외 처리 전략(Strategy)을 통해 예외 로깅 및 필요한 사이드 이펙트 수행
        handler.dispatch(e);

        // 2. 예외를 클라이언트에게 반환할 규격화된 ApiError 모델로 변환
        String uri = request.getRequestURI();
        ApiError error = mapper.dispatch(e, uri);

        return error;
    }
}
