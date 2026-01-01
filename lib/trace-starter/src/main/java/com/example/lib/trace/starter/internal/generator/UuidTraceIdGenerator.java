package com.example.lib.trace.starter.internal.generator;

import java.util.UUID;

import com.example.lib.trace.core.TraceIdGenerator;

/**
 * UUID를 기반으로 8자리의 짧고 고유한 Trace ID를 생성하는 기본 구현체
 */
public class UuidTraceIdGenerator implements TraceIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
