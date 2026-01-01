package com.example.lib.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.lib.common.core.exception.BaseException;

/**
 * 클래스 또는 메서드에 대해 발생하는 예외 타입을 명시적으로 지정하는 어노테이션
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Throws {
    /**
     * 변환할 예외 타입
     */
    Class<? extends BaseException> value();
}
