package com.example.lib.common.starter.internal.strategy;

import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.SystemException;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * SystemException(예상된 시스템 예외)에 대한 처리를 담당하는 기본 전략
 */
@Slf4j
@Order(100)
public class SystemExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemException;
    }

    @Override
    public void handle(Exception e) {
    }
}
