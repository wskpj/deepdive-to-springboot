package com.example.lib.redis.starter.internal.operator;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.example.lib.redis.core.binding.KeyBinding;
import com.example.lib.redis.core.binding.ValueBinding;
import com.example.lib.redis.core.key.RedisSetKey;

/**
 * RedisSetKey에 정의된 포맷에 맞춰 레디스의 Set(집합) 자료구조를 제어하는 오퍼레이터
 * add, remove, members 등 집합 특화 연산을 제공합니다.
 */
@SuppressWarnings("null")
public class StandardRedisSetOperator extends AbstractRedisOperator<RedisSetKey> {

    public StandardRedisSetOperator(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public Long add(KeyBinding<? extends RedisSetKey> b, ValueBinding v) {
        return redisTemplate.opsForSet().add(b.key(), v.value());
    }

    public Long remove(KeyBinding<? extends RedisSetKey> b, Object... values) {
        return redisTemplate.opsForSet().remove(b.key(), values);
    }

    public Set<Object> members(KeyBinding<? extends RedisSetKey> b) {
        return redisTemplate.opsForSet().members(b.key());
    }

    public Boolean isMember(KeyBinding<? extends RedisSetKey> b, Object value) {
        return redisTemplate.opsForSet().isMember(b.key(), value);
    }

    public Long size(KeyBinding<? extends RedisSetKey> b) {
        return redisTemplate.opsForSet().size(b.key());
    }
}
