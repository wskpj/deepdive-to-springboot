package com.example.lib.redis.core.script;

public interface RedisScriptDefinition {

    String getFileName();

    String getFullPath();

    Class<?> getResultType();
}
