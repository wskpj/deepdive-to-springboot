package com.example.lib.web.starter.internal.mapper;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.lib.web.core.ApiError;
import com.example.lib.web.core.ExceptionResponseMapper;
import com.example.lib.web.core.WebError;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationExceptionResponseMapper implements ExceptionResponseMapper {

    @Override
    public boolean supports(Exception e) {
        return e instanceof MethodArgumentNotValidException ||
               e instanceof BindException ||
               e instanceof ConstraintViolationException;
    }

    @Override
    public ApiError map(Exception e, String uri) {
        ApiError error = ApiError.of(WebError.BAD_REQUEST, uri);

        if (e instanceof BindException ex) {
            ex.getFieldErrors().forEach(fieldError ->
                error.addDetail(fieldError.getField(), fieldError.getDefaultMessage())
            );
        }
        else if (e instanceof ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(violation ->
                error.addDetail(violation.getPropertyPath().toString(), violation.getMessage())
            );
        }

        return error;
    }
}
