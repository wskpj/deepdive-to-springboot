package com.example.lib.common.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.ExceptionHandleStrategy;
import com.example.lib.common.core.SystemException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class SystemExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemException;
    }

    @Override
    public void handle(Exception e) {
        log.error("System Exception: {}", e.getMessage(), e);
    }
}
