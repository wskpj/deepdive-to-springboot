package com.example.lib.trace.starter.internal.log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import org.slf4j.MDC;

import com.example.lib.common.core.context.LocalContext;
import com.example.lib.trace.core.TraceContext;
import com.example.lib.trace.core.LogTrace;
import com.example.lib.trace.core.TraceConstants;
import com.example.lib.trace.core.TraceIdGenerator;
import com.example.lib.trace.core.TraceLevel;
import com.example.lib.trace.core.TraceStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ThreadLocal과 스택(Deque)을 이용하여 스레드 안전하게 계층형 트레이싱 상태를 관리하는 LogTrace 구현체
 * 진입과 종료 시점마다 소요 시간 및 깊이(Depth)를 계산하여 로깅합니다.
 */
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
        // 1. 상위 스레드 등에서 Trace ID가 없는 경우 초기화
        syncContext();
        
        // 2. 현재 트레이스의 고유 Span ID 생성 및 계층 깊이(Depth) 증가
        String traceId = MDC.get(TraceConstants.TRACE_ID);
        String parentSpanId = MDC.get(TraceConstants.SPAN_ID);
        String spanId = (parentSpanId == null) ? traceId : UUID.randomUUID().toString().substring(0, 8);
        
        MDC.put(TraceConstants.SPAN_ID, spanId);
        if (parentSpanId != null) {
            MDC.put(TraceConstants.PARENT_SPAN_ID, parentSpanId);
        }

        int depth = TraceContext.incrementDepth();
        MDC.put(TraceConstants.DEPTH, String.valueOf(depth));
        
        // 3. 진입 로그 출력 및 상태(TraceStatus)를 스택에 보관
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
        // 1. 소요 시간 및 자식 스레드(메서드) 실행 시간 계산
        long stopTimeMillis = System.currentTimeMillis();
        long totalTimeMillis = stopTimeMillis - status.getStartTimeMillis();
        long selfTimeMillis = totalTimeMillis - status.getChildExecutionTime();
        
        // 2. 현재 스택 깊이(Depth)를 줄이고 스택에서 제거
        TraceContext.decrementDepth();
        Deque<TraceStatus> stack = stackHolder.get();
        if (!stack.isEmpty()) {
            stack.pop(); // Remove self
            if (!stack.isEmpty()) {
                stack.peek().addChildTime(totalTimeMillis);
            } else {
                stackHolder.remove(); // Root finished, clear stack
            }
        }
        
        // 3. MDC에 계산된 시간 정보를 넣고 종료 로그 출력
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

        // 4. 사용이 끝난 임시 MDC 데이터 정리 및 부모 컨텍스트 복원
        MDC.remove(TraceConstants.TOTAL_ELAPSED_TIME);
        MDC.remove(TraceConstants.ELAPSED_TIME);
        
        int currentDepth = TraceContext.getDepth();
        if (currentDepth >= 0) {
            MDC.put(TraceConstants.DEPTH, String.valueOf(currentDepth));
        } else {
            MDC.remove(TraceConstants.DEPTH);
        }
        
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

    @Override
    public void destroy() {
        TraceContext.clear();
        stackHolder.remove();
        MDC.clear();
    }

    private String getSymbols(String symbol, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(symbol).append(symbol).append(" ");
        }
        return sb.toString();
    }
}
