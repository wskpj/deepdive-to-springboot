package com.example.lib.common.starter.internal.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.example.lib.common.core.ExceptionHandleStrategy;
import com.example.lib.common.starter.internal.dispatcher.DefaultExceptionHandleDispatcher;
import com.example.lib.common.starter.internal.strategy.HandledExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.SystemExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.UnhandledExceptionStrategy;

@AutoConfiguration
public class CommonStarterConfig {

    @Bean
    @ConditionalOnMissingBean(name = "exceptionHandleDispatcher")
    public DefaultExceptionHandleDispatcher exceptionHandleDispatcher(
        List<ExceptionHandleStrategy> strategies) {
        return new DefaultExceptionHandleDispatcher(strategies);
    }

    @Bean
    @ConditionalOnMissingBean(name = "handledExceptionStrategy")
    public HandledExceptionStrategy handledExceptionStrategy() {
        return new HandledExceptionStrategy();
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "unhandledExceptionStrategy")
    public UnhandledExceptionStrategy unhandledExceptionStrategy() {
        return new UnhandledExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(name = "systemExceptionStrategy")
    public SystemExceptionStrategy systemExceptionStrategy() {
        return new SystemExceptionStrategy();
    }
}
