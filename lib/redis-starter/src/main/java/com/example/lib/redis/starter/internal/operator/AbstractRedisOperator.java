package com.example.lib.redis.starter.internal.operator;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.example.lib.redis.core.key.RedisKey;
import com.example.lib.redis.core.binding.KeyBinding;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("null")
public abstract class AbstractRedisOperator<K extends RedisKey> {

    protected final RedisTemplate<String, Object> redisTemplate;

    public Boolean hasKey(KeyBinding<? extends K> b) {
        return redisTemplate.hasKey(b.key());
    }

    public Boolean delete(KeyBinding<? extends K> b) {
        return redisTemplate.delete(b.key());
    }

    public Boolean expire(KeyBinding<? extends K> b, long timeout) {
        return redisTemplate.expire(b.key(), timeout, TimeUnit.SECONDS);
    }

    public Long getExpire(KeyBinding<? extends K> b) {
        return redisTemplate.getExpire(b.key(), TimeUnit.SECONDS);
    }
}
