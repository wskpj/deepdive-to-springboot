package com.example.lib.debug.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.example.lib.debug.starter.internal.aspect.ExceptionTraceAspect;
import com.example.lib.trace.core.ExceptionContextTracer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfiguration
@SuppressWarnings("null")
public class DebugStarterConfig {

    @Bean
    @ConditionalOnProperty(prefix = "lib.debug", name = "enabled", havingValue = "true")
    public ExceptionTraceAspect exceptionTraceAspect(ExceptionContextTracer tracer) {
        return new ExceptionTraceAspect(tracer);
    }
}
