package com.example.lib.trace.core;

/**
 * HTTP 요청 등의 트랜잭션을 식별할 고유한 Trace ID를 생성하는 인터페이스
 */
public interface TraceIdGenerator {

    String generate();
}
