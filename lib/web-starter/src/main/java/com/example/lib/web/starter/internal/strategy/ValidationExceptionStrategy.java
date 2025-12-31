package com.example.lib.web.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class ValidationExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BindException ||
               e instanceof ConstraintViolationException;
    }

    @Override
    public void handle(Exception e) {
        String fields = "";

        if (e instanceof BindException ex) {
            fields = ex.getFieldErrors().stream()
                    .map(FieldError::getField)
                    .distinct()
                    .toList()
                    .toString();
        } else if (e instanceof ConstraintViolationException ex) {
            fields = ex.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath().toString())
                    .distinct()
                    .toList()
                    .toString();
        }

        log.info("[Validation Exception] {} - Invalid Fields: {}",
                e.getClass().getSimpleName(),
                fields);
    }
}
