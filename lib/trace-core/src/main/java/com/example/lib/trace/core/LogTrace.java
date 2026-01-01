package com.example.lib.trace.core;

/**
 * 메서드 실행 계층과 소요 시간을 로깅하기 위한 인터페이스
 * 시작(begin)과 끝(end)을 쌍으로 호출하여 트랜잭션의 흐름을 추적
 */
public interface LogTrace {

    TraceStatus begin(String message);

    TraceStatus begin(String message, TraceLevel level);

    void end(TraceStatus status);

    void end(TraceStatus status, String extraInfo);

    void destroy();
}
