package com.example.lib.web.starter.internal.dispatcher;

import java.util.List;

import com.example.lib.web.core.ApiError;
import com.example.lib.web.core.ExceptionResponseDispatcher;
import com.example.lib.web.core.ExceptionResponseMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultExceptionResponseDispatcher implements ExceptionResponseDispatcher {

    private final List<ExceptionResponseMapper> mappers;

    @Override
    public ApiError dispatch(Exception e, String uri) {
        return mappers.stream()
                .filter(mapper -> mapper.supports(e))
                .findFirst()
                .map(mapper -> mapper.map(e, uri))
                .orElse(null);
    }
}
