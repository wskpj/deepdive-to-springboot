package com.example.lib.common.starter.internal.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.common.starter.internal.aspect.ThrowsAspect;
import com.example.lib.common.starter.internal.dispatcher.DefaultExceptionHandleDispatcher;
import com.example.lib.common.starter.internal.strategy.HandledExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.SystemExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.UnhandledExceptionStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

@AutoConfiguration
public class CommonStarterConfig {

    @Bean
    public ThrowsAspect throwsAspect() {
        return new ThrowsAspect();
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionHandleDispatcher.class)
    public ExceptionHandleDispatcher exceptionHandleDispatcher(
        List<ExceptionHandleStrategy> strategies) {
        return new DefaultExceptionHandleDispatcher(strategies);
    }

    @Bean
    @ConditionalOnMissingBean(HandledExceptionStrategy.class)
    public ExceptionHandleStrategy handledExceptionStrategy(
        ExceptionContextTracer tracer) {
        return new HandledExceptionStrategy(tracer);
    }
    
    @Bean
    @ConditionalOnMissingBean(UnhandledExceptionStrategy.class)
    public ExceptionHandleStrategy unhandledExceptionStrategy(
        ExceptionContextTracer tracer) {
        return new UnhandledExceptionStrategy(tracer);
    }

    @Bean
    @ConditionalOnMissingBean(SystemExceptionStrategy.class)
    public ExceptionHandleStrategy systemExceptionStrategy(
        ExceptionContextTracer tracer) {
        return new SystemExceptionStrategy(tracer);
    }
}
