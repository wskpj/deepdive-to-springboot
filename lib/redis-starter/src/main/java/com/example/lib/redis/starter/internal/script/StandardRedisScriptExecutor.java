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

@RequiredArgsConstructor
public class StandardRedisScriptExecutor {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<RedisScriptDefinition, RedisScript<?>> scriptCache = new ConcurrentHashMap<>();

    @SuppressWarnings({ "unchecked", "null" })
    public <T> T execute(RedisScriptDefinition script, List<KeyBinding<?>> bindings, List<Object> values) {
        RedisScript<T> redisScript = (RedisScript<T>) scriptCache
                .computeIfAbsent(script, s -> {
                    DefaultRedisScript<Object> rs = new DefaultRedisScript<>();
                    rs.setLocation(new ClassPathResource(s.getFullPath()));
                    rs.setResultType((Class<Object>) s.getResultType());
                    return rs;
                });
        return redisTemplate.execute(
                redisScript,
                bindings.stream().map(KeyBinding::key).toList(),
                values.stream().map(String::valueOf).toArray());
    }

    public <T> T execute(RedisScriptDefinition script, KeyBinding<?> binding, Object... values) {
        return execute(script, List.of(binding), List.of(values));
    }
}
