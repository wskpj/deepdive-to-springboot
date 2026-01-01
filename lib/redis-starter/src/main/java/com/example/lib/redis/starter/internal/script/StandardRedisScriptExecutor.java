package com.example.lib.redis.starter.internal.script;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import com.example.lib.redis.core.binding.KeyBinding;
import com.example.lib.redis.core.script.RedisScriptDefinition;

import lombok.RequiredArgsConstructor;

/**
 * 정의된 RedisScriptDefinition 명세를 기반으로
 * 실제 Lua 스크립트를 레디스(Redis)에 실행하고 결과를 반환하는 실행기
 */
@RequiredArgsConstructor
public class StandardRedisScriptExecutor {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<RedisScriptDefinition, RedisScript<?>> scriptCache = new ConcurrentHashMap<>();

    @SuppressWarnings({ "unchecked", "null" })
    public <T> T execute(RedisScriptDefinition script, List<KeyBinding<?>> bindings, List<Object> values) {
        // 1. 메모리(Cache)에 스크립트 객체가 있는지 확인 후 없으면 새로 생성하여 캐싱
        RedisScript<T> redisScript = (RedisScript<T>) scriptCache
                .computeIfAbsent(script, s -> {
                    DefaultRedisScript<Object> rs = new DefaultRedisScript<>();
                    rs.setLocation(new ClassPathResource(s.getFullPath()));
                    rs.setResultType((Class<Object>) s.getResultType());
                    return rs;
                });
                
        // 2. 바인딩된 키 목록과 값들을 추출하여 Lua 스크립트 실행
        return redisTemplate.execute(
                redisScript,
                bindings.stream().map(KeyBinding::key).toList(),
                values.stream().map(String::valueOf).toArray());
    }

    public <T> T execute(RedisScriptDefinition script, KeyBinding<?> binding, Object... values) {
        return execute(script, List.of(binding), List.of(values));
    }
}
