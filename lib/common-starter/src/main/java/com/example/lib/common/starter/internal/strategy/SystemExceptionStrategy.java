package com.example.lib.common.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.SystemException;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RequiredArgsConstructor
public class SystemExceptionStrategy implements ExceptionHandleStrategy {

    private final ExceptionContextTracer tracer;

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemException;
    }

    @Override
    public void handle(Exception e) {
        SystemException ex = (SystemException) e;
        log.error("[System Exception] {}", ex.getErrorType().getCode());
        tracer.handle(e);
    }
}
