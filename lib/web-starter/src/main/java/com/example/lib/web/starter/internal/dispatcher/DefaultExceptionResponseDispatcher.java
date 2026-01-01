package com.example.lib.web.starter.internal.dispatcher;

import java.util.List;

import com.example.lib.web.core.dispatcher.ExceptionResponseDispatcher;
import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;

import lombok.RequiredArgsConstructor;

/**
 * 예외 발생 시 여러 ExceptionResponseMapper 중 지원 가능한 매퍼를 찾아
 * ApiError 변환을 위임하는 기본 디스패처 구현체
 */
@RequiredArgsConstructor
public class DefaultExceptionResponseDispatcher implements ExceptionResponseDispatcher {

    private final List<ExceptionResponseMapper> mappers;

    @Override
    public ApiError dispatch(Exception e, String uri) {
        // 1. 등록된 매퍼들을 순회하며 해당 예외를 지원하는(supports) 매퍼 검색
        // 2. 찾은 매퍼를 통해 ApiError 응답 DTO 생성
        return mappers.stream()
                .filter(mapper -> mapper.supports(e))
                .findFirst()
                .map(mapper -> mapper.map(e, uri))
                .orElse(null);
    }
}
