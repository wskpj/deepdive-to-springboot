package com.example.lib.trace.starter.internal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.example.lib.trace.core.LogTrace;
import com.example.lib.trace.core.Trace;
import com.example.lib.trace.core.TraceLevel;
import com.example.lib.trace.core.TraceStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Trace 어노테이션이 선언된 메서드의 실행 전후로 LogTrace를 호출하여 계층형 로그를 남기는 AOP 컴포넌트
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class TraceAspect {

    private final LogTrace logTrace;

    @Around("@within(com.example.lib.trace.core.Trace) || @annotation(com.example.lib.trace.core.Trace)")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Trace trace = signature.getMethod().getAnnotation(Trace.class);
        if (trace == null) {
            trace = joinPoint.getTarget().getClass().getAnnotation(Trace.class);
        }

        TraceLevel level = (trace != null) ? trace.level() : TraceLevel.DEBUG;
        return proceedWithTrace(joinPoint, level);
    }

    private Object proceedWithTrace(ProceedingJoinPoint joinPoint, TraceLevel level) throws Throwable {
        TraceStatus status = null;
        try {
            // 1. 메서드 시그니처를 기반으로 트레이스 시작
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message, level);

            // 2. 실제 메서드 실행
            Object result = joinPoint.proceed();

            // 3. 정상 종료 시 트레이스 완료 처리
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            // 4. 예외 발생 시 예외 메시지와 함께 트레이스 종료
            if (status != null) {
                // You could add an exception() method to LogTrace if needed
                logTrace.end(status, "[EXCEPTION: " + e.getMessage() + "]");
            }
            throw e;
        }
    }
}
