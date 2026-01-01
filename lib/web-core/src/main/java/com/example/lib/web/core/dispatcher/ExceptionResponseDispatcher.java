package com.example.lib.web.core.dispatcher;

import com.example.lib.web.core.dto.ApiError;

/**
 * 예외 발생 시 해당 예외를 처리할 수 있는 매퍼를 찾아 ApiError로 변환하는 디스패처
 */
public interface ExceptionResponseDispatcher {

    ApiError dispatch(Exception e, String uri);
}
