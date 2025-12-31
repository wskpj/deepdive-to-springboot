package com.example.lib.web.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;

import com.example.lib.common.core.ExceptionHandleStrategy;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class ValidationExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BindException ||
               e instanceof ConstraintViolationException;
    }

    @Override
    public void handle(Exception e) {
        log.info("Validation Exception: {}", e.getMessage());
    }
}
