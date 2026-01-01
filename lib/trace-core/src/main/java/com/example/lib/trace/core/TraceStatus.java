package com.example.lib.trace.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents the status of a single trace point.
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
