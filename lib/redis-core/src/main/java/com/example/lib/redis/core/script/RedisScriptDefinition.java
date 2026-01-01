package com.example.lib.redis.core.script;

/**
 * Lua 스크립트 파일의 위치, 전체 경로 및 실행 결과 타입을 정의하는 명세 인터페이스
 */
public interface RedisScriptDefinition {

    String getFileName();

    String getFullPath();

    Class<?> getResultType();
}
