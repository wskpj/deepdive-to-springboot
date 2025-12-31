package com.example.lib.common.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.ExceptionHandleStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class UnhandledExceptionStrategy implements ExceptionHandleStrategy {

    private final ExceptionContextTracer tracer;

    @Override
    public boolean supports(Exception e) {

        return true;
    }

    @Override
    public void handle(Exception e) {
        log.error("[Unhandled Exception] {}", e.getClass().getName(), e);
        tracer.handle(e);
    }
}
