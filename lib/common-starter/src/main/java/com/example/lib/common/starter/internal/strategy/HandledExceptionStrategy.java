package com.example.lib.common.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.ExceptionHandleStrategy;
import com.example.lib.common.core.HandledException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class HandledExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof HandledException;
    }

    @Override
    public void handle(Exception e) {
        log.warn("Handled Exception: {}", e.getMessage());
    }
}
