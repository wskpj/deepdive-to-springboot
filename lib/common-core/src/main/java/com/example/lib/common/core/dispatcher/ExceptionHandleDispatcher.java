package com.example.lib.common.core.dispatcher;

/**
 * 예외 처리 분배 인터페이스
 */
public interface ExceptionHandleDispatcher {
    /**
     * 예외를 받아 적절한 처리 전략으로 분배합니다.
     */
    void dispatch(Exception e);
}
