package com.example.lib.redis.core.binding;

import com.example.lib.redis.core.key.RedisKey;

public record KeyBinding<K extends RedisKey>(
    K metadata,
    String actualKey
) {
    public String key() {
        return actualKey;
    }
}