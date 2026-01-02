package com.example.lib.common.starter.internal.strategy;

import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.HandledException;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * HandledException(예상된 비즈니스 예외)에 대한 처리를 담당하는 기본 전략
 */
@Slf4j
@Order(100)
public class HandledExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof HandledException;
    }

    @Override
    public void handle(Exception e) {
    }
}
