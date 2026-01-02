package com.example.lib.web.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 스프링의 유효성 검증 실패 예외(BindException, ConstraintViolationException) 발생 시
 * 에러가 발생한 필드 목록을 추출하여 로깅하는 처리 전략
 */
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

        // 1. BindException인 경우 필드명 추출 후 중복 제거
        if (e instanceof BindException ex) {
            fields = ex.getFieldErrors().stream()
                    .map(FieldError::getField)
                    .distinct()
                    .toList()
                    .toString();
        } 
        // 2. ConstraintViolationException인 경우 경로 추출 후 중복 제거
        else if (e instanceof ConstraintViolationException ex) {
            fields = ex.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath().toString())
                    .distinct()
                    .toList()
                    .toString();
        }

        // 3. 추출된 잘못된 필드 목록을 DEBUG 레벨로 로깅
        log.debug("[Validation Exception] {} - Invalid Fields: {}",
                e.getClass().getSimpleName(),
                fields);
    }
}
