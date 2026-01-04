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

        // 2. 등록된 전략들을 순회하며 해당 예외를 지원하는(supports) 전략 검색
        // 3. 찾은 전략을 통해 예외 로깅 및 사이드 이펙트 처리 수행
        log.debug("[{}:>>] Start to Handle Exception [{}]",
                this.getClass().getSimpleName(),
                e.getClass().getSimpleName());
        strategies.stream()
                .filter(strategy -> {
                    boolean supported = strategy.supports(e);
                    if (supported) {
                        log.debug("[{}:--] Found Strategy [{}]", 
                                this.getClass().getSimpleName(), 
                                strategy.getClass().getSimpleName());
                    }
                    return supported;
                })
                .findFirst()
                .ifPresent(strategy -> {
                    log.debug("[{}:<<] Handling Exception [{}] with Strategy [{}]",
                            this.getClass().getSimpleName(),
                            e.getClass().getSimpleName(),
                            strategy.getClass().getSimpleName());
                    strategy.handle(e);
                });
    }
}
