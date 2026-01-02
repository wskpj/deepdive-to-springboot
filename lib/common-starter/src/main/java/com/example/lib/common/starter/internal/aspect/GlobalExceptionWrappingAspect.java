package com.example.lib.common.starter.internal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.UnhandledException;

import lombok.extern.slf4j.Slf4j;

/**
 * 글로벌 예외 래핑 어스펙트
 * BaseException이 아닌 표준 예외를 UnhandledException으로 래핑하여, 일관된 예외 처리 흐름을 보장합니다.
 */
@Slf4j
@Aspect
@Order(1)
public class GlobalExceptionWrappingAspect {

    @Around("within(com.example..*) " +
            "&& !within(com.example..config..*) " +
            "&& !within(com.example.lib..starter..*)")
    public Object wrap(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.debug("[{}:>>] Exception Occured and Trying to Wrap.. ",
                    this.getClass().getSimpleName());

            if (e instanceof BaseException be) {
                // BaseException 타입인 경우 그대로 throw
                log.debug("[{}:<<] Detected BaseException, Passing..",
                        this.getClass().getSimpleName());
                throw e;
            }

            // BaseException이 아닌 모든 자바 표준 예외를 낚아채 UnhandledException으로 래핑
            log.debug("[{}:<<] Detected UnhandledException, Wrapping into UnhandledException: {}",
                    this.getClass().getSimpleName(),
                    e.getMessage());
            throw new UnhandledException(e);
        }
    }
}
