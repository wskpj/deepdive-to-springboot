package com.example.lib.common.starter.internal.dispatcher;

import java.util.List;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DefaultExceptionHandleDispatcher implements ExceptionHandleDispatcher {

    private final List<ExceptionHandleStrategy> strategies;
    private final ExceptionContextTracer tracer;

    @Override
    public void dispatch(Exception e) {
        // Trace Context First
        tracer.handle(e);

        // Then Handle
        strategies.stream()
                .filter(strategy -> strategy.supports(e))
                .findFirst()
                .ifPresent(strategy -> strategy.handle(e));
    }
}
