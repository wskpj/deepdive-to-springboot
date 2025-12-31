package com.example.lib.redis.starter.internal.operator;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.example.lib.redis.core.binding.KeyBinding;
import com.example.lib.redis.core.binding.ValueBinding;
import com.example.lib.redis.core.key.RedisStringKey;

@SuppressWarnings("null")
public class StandardRedisStringOperator extends AbstractRedisOperator<RedisStringKey> {

    public StandardRedisStringOperator(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public Object get(KeyBinding<? extends RedisStringKey> b) {
        return redisTemplate.opsForValue().get(b.key());
    }

    public <T> T get(KeyBinding<? extends RedisStringKey> b, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(b.key());
        if (value == null) return null;
        return clazz.cast(value);
    }

    public void set(KeyBinding<? extends RedisStringKey> b, ValueBinding v) {
        redisTemplate.opsForValue().set(b.key(), v.value());
    }

    public void set(KeyBinding<? extends RedisStringKey> b, ValueBinding v, long timeout) {
        redisTemplate.opsForValue().set(b.key(), v.value(), timeout, TimeUnit.SECONDS);
    }

    public Boolean setIfAbsent(KeyBinding<? extends RedisStringKey> b, ValueBinding v) {
        return redisTemplate.opsForValue().setIfAbsent(b.key(), v.value());
    }

    public boolean setIfAbsent(KeyBinding<? extends RedisStringKey> b, ValueBinding v, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(b.key(), v.value(), timeout, TimeUnit.SECONDS));
    }

    public Long increment(KeyBinding<? extends RedisStringKey> b) {
        return increment(b, 1L);
    }

    public Long increment(KeyBinding<? extends RedisStringKey> b, long delta) {
        return redisTemplate.opsForValue().increment(b.key(), delta);
    }

    public Long decrement(KeyBinding<? extends RedisStringKey> b) {
        return decrement(b, 1L);
    }

    public Long decrement(KeyBinding<? extends RedisStringKey> b, long delta) {
        return redisTemplate.opsForValue().increment(b.key(), -delta);
    }
}
