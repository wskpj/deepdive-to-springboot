package com.example.lib.common.starter.internal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.annotation.Throws;
import com.example.lib.common.core.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Throws 어노테이션이 선언된 메서드에서 발생하는 예외를 가로채어,
 * 어노테이션에 지정된 BaseException 타입으로 변환(Translate)하는 AOP 컴포넌트
 */
@Slf4j
@Aspect
@Order(2)
public class ThrowsAspect {

    /**
     * 타겟 메서드를 실행하고 예외 발생 시 지정된 예외 타입으로 래핑하여 다시 던집니다.
     */
    @Around("@within(throwsAnnotation) || @annotation(throwsAnnotation)")
    public Object translate(ProceedingJoinPoint joinPoint, Throws throwsAnnotation) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 1. 원래 예외가 이미 BaseException 계열이면 변환 없이 그대로 던짐
            if (e instanceof BaseException) throw e;

            Class<? extends BaseException> targetExceptionClass = throwsAnnotation.value();

            log.debug("[{}] Caught and Translating Exception: {} -> {}",
                    this.getClass().getSimpleName(),
                    e.getClass().getSimpleName(),
                    targetExceptionClass.getSimpleName());

            BaseException wrappedException;
            try {
                // 2. 지정된 예외 타입으로 래핑 시도
                wrappedException = translateWrappedException(targetExceptionClass, e);
            } catch (Exception translationEx) {
                // 3. 변환 실패 시 Fallback으로 원래 예외를 던짐
                log.error("[{}] Failed to translate exception. Re-throwing original exception. Reason: {}",
                        this.getClass().getSimpleName(),
                        translationEx.getMessage());
                throw e; // Handled by ExceptionHandleStrategy
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

            // 1. 기본 생성자로 인스턴스 생성
            BaseException baseException = targetClass.getConstructor().newInstance();
            try {
                // 2. initCause로 원인 예외 설정
                baseException.initCause(e);
            } catch (IllegalStateException ignored) {
                // 3. 이미 initCause가 호출되었거나, 지원하지 않는 경우 무시
            }
            return baseException;
        }
    }
}
