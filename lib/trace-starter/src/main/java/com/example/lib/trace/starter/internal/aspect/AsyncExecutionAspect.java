package com.example.lib.trace.starter.internal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;

import com.example.lib.trace.core.TraceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class AsyncExecutionAspect {

    @Around("@annotation(org.springframework.scheduling.annotation.Async)")
    public Object traceAsyncExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;            
            
            MDC.put(TraceConstants.ELAPSED_TIME, String.valueOf(elapsedTime));

            log.info("[ASYNC] {} - {}ms", joinPoint.getSignature().toShortString(), elapsedTime);

            MDC.remove(TraceConstants.ELAPSED_TIME);
        }
    }
}
