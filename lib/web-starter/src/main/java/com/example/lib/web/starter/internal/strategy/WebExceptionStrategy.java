package com.example.lib.web.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.exception.WebException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 50)
public class WebExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof WebException;
    }

    @Override
    public void handle(Exception e) {
        WebException we = (WebException) e;

        switch (we.getErrorType()) {
            case WebError.UNAUTHORIZED, WebError.FORBIDDEN, WebError.TOO_MANY_REQUESTS:
                log.warn("[Web Exception] {}", we.getErrorType().getCode());
                break;
            default:
                // log.info("[Web Exception] {}", we.getErrorType().getCode());
                break;
        }
    }
}
