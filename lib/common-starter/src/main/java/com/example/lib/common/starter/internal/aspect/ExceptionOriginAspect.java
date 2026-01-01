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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(1)
public class ExceptionOriginAspect {

    @AfterThrowing(pointcut = "within(com.example..*) && !within(*..*Properties)", throwing = "be")
    public void captureOrigin(JoinPoint joinPoint, BaseException be) {
        if (be.getOrigin() != null) return;

        try {
            for (StackTraceElement ste : be.getStackTrace()) {
                String cn = ste.getClassName();
                if (cn.startsWith("com.example") && !cn.endsWith("Exception") && !cn.contains("BaseException")) {
                    be.setOrigin(new ExceptionOrigin(cn, ste.getMethodName(), ste.getLineNumber(), extractArguments(joinPoint)));
                    return;
                }
            }
        } catch (Exception ignored) {
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
