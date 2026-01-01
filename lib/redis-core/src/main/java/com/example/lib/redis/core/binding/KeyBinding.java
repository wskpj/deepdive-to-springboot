package com.example.lib.redis.core.binding;

import com.example.lib.redis.core.key.RedisKey;

/**
 * RedisKey(포맷)와 실제 주입될 파라미터를 결합하여
 * 런타임에 결정된 최종 Redis Key 문자열을 보관하는 바인딩 객체
 */
public record KeyBinding<K extends RedisKey>(
    K metadata,
    String actualKey
) {
    public String key() {
        return actualKey;
    }
}