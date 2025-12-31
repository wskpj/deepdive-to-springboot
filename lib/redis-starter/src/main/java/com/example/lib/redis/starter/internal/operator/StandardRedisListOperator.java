package com.example.lib.redis.starter.internal.operator;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

import com.example.lib.redis.core.binding.KeyBinding;
import com.example.lib.redis.core.binding.ValueBinding;
import com.example.lib.redis.core.key.RedisListKey;

@SuppressWarnings("null")
public class StandardRedisListOperator extends AbstractRedisOperator<RedisListKey> {

    public StandardRedisListOperator(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public Long leftPush(KeyBinding<? extends RedisListKey> b, ValueBinding v) {
        return redisTemplate.opsForList().leftPush(b.key(), v.value());
    }

    public Long rightPush(KeyBinding<? extends RedisListKey> b, ValueBinding v) {
        return redisTemplate.opsForList().rightPush(b.key(), v.value());
    }

    public Object leftPop(KeyBinding<? extends RedisListKey> b) {
        return redisTemplate.opsForList().leftPop(b.key());
    }

    public Object rightPop(KeyBinding<? extends RedisListKey> b) {
        return redisTemplate.opsForList().rightPop(b.key());
    }

    public List<Object> range(KeyBinding<? extends RedisListKey> b, long start, long end) {
        return redisTemplate.opsForList().range(b.key(), start, end);
    }

    public Long size(KeyBinding<? extends RedisListKey> b) {
        return redisTemplate.opsForList().size(b.key());
    }

    public void trim(KeyBinding<? extends RedisListKey> b, long start, long end) {
        redisTemplate.opsForList().trim(b.key(), start, end);
    }
}
