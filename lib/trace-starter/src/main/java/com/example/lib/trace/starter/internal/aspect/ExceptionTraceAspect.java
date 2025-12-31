package com.example.lib.trace.starter.internal.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ExceptionTraceAspect {

    private final ExceptionContextTracer tracer;

    @After("execution(* com.example.lib.common.core.strategy.ExceptionHandleStrategy.handle(Exception)) && args(e)")
    public void traceException(Exception e) {
        log.debug("[ExceptionTraceAspect] Tracing exception: {}", e.getClass().getSimpleName());
        tracer.handle(e);
    }
}
