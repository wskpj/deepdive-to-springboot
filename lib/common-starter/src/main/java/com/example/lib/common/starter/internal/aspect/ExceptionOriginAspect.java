package com.example.lib.common.starter.internal.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.ExceptionOrigin;
import com.example.lib.common.core.exception.HandledException;

import lombok.extern.slf4j.Slf4j;

/**
 * 예외 발생 지점(Origin)을 추적하고 추출하는 AOP 컴포넌트
 * 예외가 던져지는 시점(@AfterThrowing)에 개입하여 클래스, 메서드, 파라미터 정보 수집
 */
@Slf4j
@Aspect
@Order(1)
public class ExceptionOriginAspect {

    /**
     * 예외 발생 시 Origin 정보를 추출하여 BaseException에 저장합니다.
     */
    @AfterThrowing(pointcut = "within(com.example..*) && !within(*..*Properties)", throwing = "ex")
    public void captureOrigin(JoinPoint joinPoint, Exception ex) {
        log.debug("[{}:>>] Starting to capture Exception Origin", this.getClass().getSimpleName());

        // 1. 이미 Origin이 설정되어 있거나 BaseException이 아닌 경우 무시
        if (!(ex instanceof BaseException be)) return;
        if (be.getOrigin() != null) return;

        try {
            // 2. JoinPoint를 통해 발생 클래스, 메서드 및 파라미터(Args) 추출
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            Map<String, Object> args = extractArguments(joinPoint);
            
            // 3. HandledException인 경우 스택 트레이스 없이 Origin을 즉시 기록
            if (ex instanceof HandledException) {
                be.setOrigin(new ExceptionOrigin(className, methodName, 0, args));
                log.debug("[{}:<<] Finished to capture Exception Origin: [{}:{}] {}() with args {}", this.getClass().getSimpleName(), className, 0, methodName, args);
                return;
            }
        } catch (Exception ignored) {
            log.warn("[{}:<<] Failed to capture Exception Origin",this.getClass().getSimpleName());
        }
    }

    private Map<String, Object> extractArguments(JoinPoint joinPoint) {
        Map<String, Object> map = new HashMap<>();

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            if (parameterNames != null && args != null) {
                for (int i = 0; i < Math.min(parameterNames.length, args.length); i++) {
                    map.put(parameterNames[i], args[i]);
                }
            }
        } catch (Exception e) {
            map.put("_error", "failed to extract args");
        }
        return map;
    }
}
