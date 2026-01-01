package com.example.lib.debug.starter.internal.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.ExceptionOrigin;
import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ExceptionTraceAspect {

    private final ExceptionContextTracer tracer;

    @After("execution(* com.example.lib.common.core.strategy.ExceptionHandleStrategy.handle(Exception)) && args(e)")
    public void traceException(Exception e) {
        if (!log.isDebugEnabled()) {
            return;
        }

        if (e instanceof BaseException be && be.getOrigin() != null) {
            ExceptionOrigin origin = be.getOrigin();
            log.debug("[{}] Tracing [{}] occurred at origin [{}:{}] method {}() with args: {}",
                    this.getClass().getSimpleName(),
                    be.getClass().getSimpleName(),
                    getOriginSimpleClassName(origin.className()),
                    origin.lineNumber(),
                    origin.methodName(),
                    origin.arguments());
        }
        else {
            for (StackTraceElement ste : e.getStackTrace()) {
                String cn = ste.getClassName();
                if (cn.startsWith("com.example") && !cn.endsWith("Exception") && !cn.contains("BaseException")) {
                    log.debug("[{}] Tracing Exception occurred at origin [{}:{}] method {}() with no args", 
                            this.getClass().getSimpleName(),
                            getOriginSimpleClassName(cn),
                            ste.getLineNumber(),
                            ste.getMethodName());
                    break;
                }
            }
        }

        tracer.handle(e);
    }

    private String getOriginSimpleClassName(String className) {
        return className.contains(".") ? className.substring(className.lastIndexOf(".") + 1) : className;
    }
}
