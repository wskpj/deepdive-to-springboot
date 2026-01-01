package com.example.lib.trace.starter.internal.log;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;

import lombok.extern.slf4j.Slf4j;

/**
 * MDC를 이용하여 즉시 로깅을 수행하는 클래스
 * 메시지와 함께 컨텍스트를 전달하면 일회성 컨텍스트를 MDC에 설정 후 로깅을 수행함
 */
@Slf4j
public class MDCInstantLogger {

    public static void debug(String message, Map<String, ?> context) {
        execute(context, () -> log.debug(message));
    }

    public static void info(String message, Map<String, ?> context) {
        execute(context, () -> log.info(message));
    }

    public static void warn(String message, Map<String, ?> context) {
        execute(context, () -> log.warn(message));
    }

    public static void error(String message, Map<String, ?> context) {
        execute(context, () -> log.error(message));
    }

    private static void execute(Map<String, ?> context, Runnable logAction) {
        if (context == null || context.isEmpty()) {
            logAction.run();
            return;
        }

        Map<String, String> previousContext = new HashMap<>();
        try {
            context.forEach((k, v) -> {
                previousContext.put(k, MDC.get(k));
                MDC.put(k, String.valueOf(v));
            });
            logAction.run();
        } finally {
            previousContext.forEach((k, v) -> {
                if (v == null) {
                    MDC.remove(k);
                } else {
                    MDC.put(k, v);
                }
            });
        }
    }
}
