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
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message, level);
            
            Object result = joinPoint.proceed();
            
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            if (status != null) {
                // You could add an exception() method to LogTrace if needed
                logTrace.end(status, "[EXCEPTION: " + e.getMessage() + "]");
            }
            throw e;
        }
    }
}
