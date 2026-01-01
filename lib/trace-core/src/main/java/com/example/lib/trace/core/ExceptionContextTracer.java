package com.example.lib.trace.core;

/**
 * 예외 발생 시 필요한 컨텍스트 정보(Origin, Request 등)를 수집하고 로깅하기 위한 추적기 인터페이스
 */
public interface ExceptionContextTracer {

    /**
     * 예외를 인자로 받아 컨텍스트를 구성하고 추적/로깅합니다.
     */
    void handle(Exception e);
}
