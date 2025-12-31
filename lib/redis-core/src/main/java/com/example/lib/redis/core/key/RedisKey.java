package com.example.lib.redis.core.key;

import com.example.lib.redis.core.binding.KeyBinding;

public sealed interface RedisKey permits RedisStringKey, RedisListKey, RedisSetKey {

    String getPattern();

    @SuppressWarnings("unchecked")
    default <K extends RedisKey> KeyBinding<K> bind(Object... args) {
        return (KeyBinding<K>) new KeyBinding<>(this, String.format(getPattern(), args));
    }
}
