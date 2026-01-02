package com.example.lib.web.starter.internal.strategy;

import org.springframework.core.annotation.Order;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.core.exception.WebException;

import lombok.extern.slf4j.Slf4j;

/**
 * 웹 계층에서 명시적으로 발생시킨 WebException 처리 전략
 * 인증/인가 예외나 요청 한도 초과 등은 WARN 레벨로, 그 외 일반 오류는 INFO 레벨로 로깅함
 */
@Slf4j
@Order(10000)
public class WebExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof WebException;
    }

    @Override
    public void handle(Exception e) {
    }
}
