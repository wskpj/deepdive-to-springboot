package com.example.lib.redis.core.key;

import com.example.lib.redis.core.binding.KeyBinding;

/**
 * 레디스(Redis) 키의 포맷(Pattern)을 정의하는 최상위 인터페이스
 * bind() 메서드를 통해 파라미터를 주입받아 KeyBinding 객체를 생성합니다.
 */
public sealed interface RedisKey permits RedisStringKey, RedisListKey, RedisSetKey {

    String getPattern();

    @SuppressWarnings("unchecked")
    default <K extends RedisKey> KeyBinding<K> bind(Object... args) {
        return (KeyBinding<K>) new KeyBinding<>(this, String.format(getPattern(), args));
    }
}
