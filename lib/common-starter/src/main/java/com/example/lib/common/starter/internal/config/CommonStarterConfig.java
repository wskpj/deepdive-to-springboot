package com.example.lib.common.starter.internal.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.example.lib.common.core.ExceptionHandleDispatcher;
import com.example.lib.common.core.ExceptionHandleStrategy;
import com.example.lib.common.starter.internal.dispatcher.DefaultExceptionHandleDispatcher;
import com.example.lib.common.starter.internal.strategy.HandledExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.SystemExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.UnhandledExceptionStrategy;

@AutoConfiguration
public class CommonStarterConfig {

    @Bean
    @ConditionalOnMissingBean(ExceptionHandleDispatcher.class)
    public ExceptionHandleDispatcher exceptionHandleDispatcher(
        List<ExceptionHandleStrategy> strategies) {
        return new DefaultExceptionHandleDispatcher(strategies);
    }

    @Bean
    @ConditionalOnMissingBean(HandledExceptionStrategy.class)
    public ExceptionHandleStrategy handledExceptionStrategy() {
        return new HandledExceptionStrategy();
    }
    
    @Bean
    @ConditionalOnMissingBean(UnhandledExceptionStrategy.class)
    public ExceptionHandleStrategy unhandledExceptionStrategy() {
        return new UnhandledExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(SystemExceptionStrategy.class)
    public ExceptionHandleStrategy systemExceptionStrategy() {
        return new SystemExceptionStrategy();
    }
}
