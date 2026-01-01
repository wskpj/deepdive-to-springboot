package com.example.lib.common.starter.internal.strategy;

import org.slf4j.event.Level;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.SystemException;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class SystemExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemException;
    }

    @Override
    public void handle(Exception e) {
        BaseException ex = (BaseException) e;
        log.atLevel(Level.valueOf(ex.getLogLevel().name()))
           .log("[System Exception] {}", ex.getErrorType().getCode());
    }
}
