package com.example.lib.common.starter.internal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.example.lib.common.core.annotation.Throws;
import com.example.lib.common.core.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class ThrowsAspect {

    @Around("@within(throwsAnnotation) || @annotation(throwsAnnotation)")
    public Object translate(ProceedingJoinPoint joinPoint, Throws throwsAnnotation) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            if (e instanceof BaseException) throw e;

            Class<? extends BaseException> targetExceptionClass = throwsAnnotation.value();

            log.warn("Caught and Translating Exception: {} -> {}",
                    e.getClass().getSimpleName(),
                    targetExceptionClass.getSimpleName());

            BaseException wrappedException;
            try {
                wrappedException = translateWrappedException(targetExceptionClass, e);
            } catch (Exception translationEx) {
                log.warn("Failed to translate exception. Re-throwing original exception. Reason: {}", translationEx.getMessage());
                throw e; // Handled Original Exception with ExceptionHandleStrategy
            }
            throw wrappedException;
        }
    }

    private BaseException translateWrappedException(Class<? extends BaseException> targetClass, Exception e) throws Exception {
        try {
            // Try Throwable Cause Constructor
            return targetClass.getConstructor(Throwable.class).newInstance(e);
        } catch (NoSuchMethodException ex) {
            // Fallback to Default Constructor
            return targetClass.getConstructor().newInstance();
        }
    }
}
