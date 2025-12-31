package com.example.lib.trace.starter.internal.decorator;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncTraceDecorator implements TaskDecorator {

    @Override
    @SuppressWarnings("null")
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
