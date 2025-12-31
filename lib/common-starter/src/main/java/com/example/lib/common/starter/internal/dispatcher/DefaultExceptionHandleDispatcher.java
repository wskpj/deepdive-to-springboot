package com.example.lib.common.starter.internal.dispatcher;

import java.util.List;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DefaultExceptionHandleDispatcher implements ExceptionHandleDispatcher {

    private final List<ExceptionHandleStrategy> strategies;

    @Override
    public void dispatch(Exception e) {
        strategies.stream()
                .filter(strategy -> strategy.supports(e))
                .findFirst()
                .ifPresent(strategy -> strategy.handle(e));
    }
}
