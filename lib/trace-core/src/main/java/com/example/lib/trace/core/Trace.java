package com.example.lib.trace.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메서드 실행 흐름(LogTrace)을 추적할 대상을 지정하는 어노테이션
 * 클래스나 메서드 레벨에 선언하여 AOP를 통해 자동으로 트레이싱을 적용
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
    TraceLevel level() default TraceLevel.DEBUG;
}
