package com.example.lib.web.starter.internal.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.lib.common.core.ExceptionHandleDispatcher;
import com.example.lib.web.core.ApiError;
import com.example.lib.web.core.ApiResult;
import com.example.lib.web.core.ExceptionResponseDispatcher;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class StandardExceptionHandler {

    private final ExceptionHandleDispatcher handler;
    private final ExceptionResponseDispatcher mapper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<?>> handleAllException(Exception e, HttpServletRequest request) {
        handler.dispatch(e);

        String uri = request.getRequestURI();
        ApiError error = mapper.dispatch(e, uri);
        return ResponseEntity.status(error.status()).body(ApiResult.fail(error));
    }
}
