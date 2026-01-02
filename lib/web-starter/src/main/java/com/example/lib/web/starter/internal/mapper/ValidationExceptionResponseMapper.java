package com.example.lib.web.starter.internal.mapper;

import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 스프링의 유효성 검증(Validation) 실패 시 발생하는 예외들을 가로채어
 * ApiError에 각 필드별 에러 상세 내역(details)을 포함시키는 매퍼
 */
@Slf4j
@Order(10)
public class ValidationExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return e instanceof MethodArgumentNotValidException ||
               e instanceof BindException ||
               e instanceof ConstraintViolationException;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        // 1. 기본적으로 BAD_REQUEST 에러 타입으로 생성
        ApiError error = ApiError.of(WebError.BAD_REQUEST, uri);

        // 2. BindException (객체 필드 검증 실패)인 경우 각 필드명과 에러 메시지를 추출
        if (e instanceof BindException ex) {
            ex.getFieldErrors().forEach(fieldError ->
                error.addDetail(fieldError.getField(), fieldError.getDefaultMessage())
            );
        }
        // 3. ConstraintViolationException (메서드 파라미터 검증 실패)인 경우 각 파라미터 경로와 메시지를 추출
        else if (e instanceof ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(violation ->
                error.addDetail(violation.getPropertyPath().toString(), violation.getMessage())
            );
        }

        return error;
    }
}
