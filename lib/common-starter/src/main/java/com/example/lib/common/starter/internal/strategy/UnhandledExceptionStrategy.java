package com.example.lib.common.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 구체적인 처리 전략이 없는 모든 예외(Unhandled)를 처리하는 최후 전략(Fallback)
 * 모든 예외를 지원(supports = true)하므로 우선순위가 가장 낮음
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class UnhandledExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return true;
    }

    @Override
    public void handle(Exception e) {
    }
}
