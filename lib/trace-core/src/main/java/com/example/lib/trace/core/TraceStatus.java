package com.example.lib.trace.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 단일 트레이스 지점(메서드 실행 단위)의 상태 정보를 담는 객체
 * 고유 ID, 계층 깊이(Depth), 소요 시간 및 Span 정보를 보관합니다.
 */
@Getter
@RequiredArgsConstructor
public class TraceStatus {
    private final String traceId;
    private final long startTimeMillis;
    private final String message;
    private final int depth;
    private final TraceLevel level;
    
    // For Span restoration
    private final String spanId;
    private final String parentSpanId;
    
    // Track total time spent in children
    private long childExecutionTime = 0;

    public void addChildTime(long time) {
        this.childExecutionTime += time;
    }

    public static TraceStatus of(String traceId, String message, int depth, TraceLevel level, String spanId, String parentSpanId) {
        return new TraceStatus(traceId, System.currentTimeMillis(), message, depth, level, spanId, parentSpanId);
    }
}
