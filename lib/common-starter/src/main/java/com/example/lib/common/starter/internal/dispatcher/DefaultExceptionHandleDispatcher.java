package com.example.lib.common.starter.internal.dispatcher;

import java.util.List;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 예외 발생 시 적절한 ExceptionHandleStrategy를 찾아 처리를 위임하는 디스패처 기본 구현체
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultExceptionHandleDispatcher implements ExceptionHandleDispatcher {

    private final List<ExceptionHandleStrategy> strategies;
    private final ExceptionContextTracer tracer;

    /**
     * 예외를 받아 적절한 처리 전략으로 분배합니다.
     */
    @Override
    public void dispatch(Exception e) {
        // 1. 예외 컨텍스트 추적
        tracer.handle(e);

        // 2. 지원하는 처리 전략을 찾아 위임
        strategies.stream()
                .filter(strategy -> strategy.supports(e))
                .findFirst()
                .ifPresent(strategy -> strategy.handle(e));
    }
}
