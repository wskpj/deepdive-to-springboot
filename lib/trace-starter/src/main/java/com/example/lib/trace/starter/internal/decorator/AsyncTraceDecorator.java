package com.example.lib.trace.starter.internal.decorator;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import com.example.lib.common.core.context.LocalContext;
import com.example.lib.trace.core.TraceContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncTraceDecorator implements TaskDecorator {

    @Override
    @SuppressWarnings("null")
    public Runnable decorate(Runnable runnable) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        Map<String, Object> localContext = LocalContext.getAll();
        int parentDepth = TraceContext.getDepth();

        return () -> {
            try {
                if (mdcContext != null) {
                    MDC.setContextMap(mdcContext);
                }
                localContext.forEach(LocalContext::put);
                TraceContext.setDepth(parentDepth);

                runnable.run();
            } finally {
                MDC.clear();
                LocalContext.clear();
                TraceContext.clear();
            }
        };
    }
}
