package com.example.lib.web.core.mapper;

import com.example.lib.web.core.dto.ApiError;

/**
 * 특정 예외를 ApiError DTO로 변환하는 매퍼 인터페이스
 */
public interface ExceptionResponseMapper {

    boolean supports(Exception e);

    ApiError map(Exception e, String uri);
}
