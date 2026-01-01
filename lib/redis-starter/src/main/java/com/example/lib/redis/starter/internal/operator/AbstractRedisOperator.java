package com.example.lib.redis.starter.internal.operator;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.example.lib.redis.core.key.RedisKey;
import com.example.lib.redis.core.binding.KeyBinding;

import lombok.RequiredArgsConstructor;

/**
 * 레디스(Redis)의 모든 자료구조에서 공통적으로 사용 가능한 연산(Key 존재 여부, 만료 시간, 삭제 등)을 
 * 캡슐화한 최상위 오퍼레이터 추상 클래스
 */
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
