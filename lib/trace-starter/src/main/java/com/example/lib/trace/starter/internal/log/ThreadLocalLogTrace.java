package com.example.lib.trace.starter.internal.log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import org.slf4j.MDC;

import com.example.lib.common.core.context.LocalContext;
import com.example.lib.trace.core.LogTrace;
import com.example.lib.trace.core.TraceConstants;
import com.example.lib.trace.core.TraceIdGenerator;
import com.example.lib.trace.core.TraceLevel;
import com.example.lib.trace.core.TraceStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ThreadLocalLogTrace implements LogTrace {

    private final TraceIdGenerator traceIdGenerator;
    private final ThreadLocal<Deque<TraceStatus>> stackHolder = ThreadLocal.withInitial(ArrayDeque::new);

    @Override
    public TraceStatus begin(String message) {
        return begin(message, TraceLevel.INFO);
    }

    @Override
    public TraceStatus begin(String message, TraceLevel level) {
        syncContext();
        
        String traceId = MDC.get(TraceConstants.TRACE_ID);
        String parentSpanId = MDC.get(TraceConstants.SPAN_ID);
        String spanId = (parentSpanId == null) ? traceId : UUID.randomUUID().toString().substring(0, 8);
        
        // Save current span as parent for next step
        MDC.put(TraceConstants.SPAN_ID, spanId);
        if (parentSpanId != null) {
            MDC.put(TraceConstants.PARENT_SPAN_ID, parentSpanId);
        }

        int depth = LocalContext.incrementDepth();
        MDC.put(TraceConstants.DEPTH, String.valueOf(depth));
        
        String logMessage = String.format("[%s] %s %s", traceId, getSymbols(">", depth + 1), message);
        logWithLevel(level, logMessage);
        
        TraceStatus status = TraceStatus.of(traceId, message, depth, level, spanId, parentSpanId);
        stackHolder.get().push(status);
        return status;
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void end(TraceStatus status, String extraInfo) {
        complete(status, extraInfo);
    }

    private void complete(TraceStatus status, String extraInfo) {
        long stopTimeMillis = System.currentTimeMillis();
        long totalTimeMillis = stopTimeMillis - status.getStartTimeMillis();
        long selfTimeMillis = totalTimeMillis - status.getChildExecutionTime();
        
        LocalContext.decrementDepth();
        Deque<TraceStatus> stack = stackHolder.get();
        if (!stack.isEmpty()) {
            stack.pop(); // Remove self
            if (!stack.isEmpty()) {
                stack.peek().addChildTime(totalTimeMillis);
            } else {
                stackHolder.remove(); // Root finished, clear stack
            }
        }
        
        // Populate MDC for structured logging
        MDC.put(TraceConstants.DEPTH, String.valueOf(status.getDepth()));
        MDC.put(TraceConstants.TOTAL_ELAPSED_TIME, String.valueOf(totalTimeMillis));
        MDC.put(TraceConstants.ELAPSED_TIME, String.valueOf(selfTimeMillis));

        String timeDisplay = (status.getChildExecutionTime() > 0)
                ? String.format("%dms / %dms", selfTimeMillis, totalTimeMillis)
                : String.format("%dms", totalTimeMillis);

        String logMessage = String.format("[%s] %s [%s] %s%s", 
                status.getTraceId(), 
                getSymbols("<", status.getDepth() + 1),
                timeDisplay,
                status.getMessage(),
                extraInfo != null ? " " + extraInfo : "");
        
        logWithLevel(status.getLevel(), logMessage);

        // Cleanup temporary MDC fields
        MDC.remove(TraceConstants.TOTAL_ELAPSED_TIME);
        MDC.remove(TraceConstants.ELAPSED_TIME);
        
        // Restore/Update Depth in MDC for parent context
        int currentDepth = LocalContext.getDepth();
        if (currentDepth >= 0) {
            MDC.put(TraceConstants.DEPTH, String.valueOf(currentDepth));
        } else {
            MDC.remove(TraceConstants.DEPTH);
        }
        
        // Restore Span Context
        if (status.getParentSpanId() != null) {
            MDC.put(TraceConstants.SPAN_ID, status.getParentSpanId());
        } else {
            MDC.remove(TraceConstants.SPAN_ID);
            MDC.remove(TraceConstants.PARENT_SPAN_ID);
        }
    }

    private void logWithLevel(TraceLevel level, String message) {
        switch (level) {
            case TRACE -> log.trace(message);
            case DEBUG -> log.debug(message);
            case INFO -> log.info(message);
            case WARN -> log.warn(message);
            case ERROR -> log.error(message);
        }
    }

    private void syncContext() {
        String traceId = MDC.get(TraceConstants.TRACE_ID);
        if (traceId == null) {
            traceId = traceIdGenerator.generate();
            MDC.put(TraceConstants.TRACE_ID, traceId);
            LocalContext.put(TraceConstants.TRACE_ID, traceId);
        }
    }

    private String getSymbols(String symbol, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(symbol).append(symbol).append(" ");
        }
        return sb.toString();
    }
}
