package com.example.lib.redis.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Lua 스크립트를 실행하는 레디스(Redis) 오퍼레이터 메서드에
 * 명시적인 마커(Marker)로 부착하는 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LuaExecute {
    // Marker Annotation
}
