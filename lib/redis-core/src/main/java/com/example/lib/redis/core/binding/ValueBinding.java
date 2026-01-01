package com.example.lib.redis.core.binding;

import java.util.Arrays;

/**
 * 레디스(Redis)에 저장할 Value 값들을 단일 문자열 또는 
 * 콜론(:)으로 결합된 복합 문자열로 변환하여 보관하는 바인딩 객체
 */
public record ValueBinding(
    String value
) {
    public static ValueBinding of(Object... args) {
        if (args.length == 1) {
            return plain(args[0]);
        }
        return composite(args);
    }

    private static ValueBinding plain(Object value) {
        return new ValueBinding(String.valueOf(value));
    }

    private static ValueBinding composite(Object... args) {
        String combined = String.join(":",
                Arrays.stream(args)
                        .map(String::valueOf)
                        .toArray(String[]::new));
        return new ValueBinding(combined);
    }
}
