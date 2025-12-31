package com.example.lib.web.starter.internal.handler;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.trace.core.TraceConstants;
import com.example.lib.web.core.dispatcher.ExceptionResponseDispatcher;
import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.dto.ApiResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

        String traceId = MDC.get(TraceConstants.TRACE_ID);
        return ResponseEntity.status(error.status()).body(ApiResult.fail(error, traceId));
    }
}
