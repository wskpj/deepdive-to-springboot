package com.example.lib.common.starter.internal.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.common.starter.internal.aspect.GlobalExceptionWrapperAspect;
import com.example.lib.common.starter.internal.aspect.ThrowsAspect;
import com.example.lib.common.starter.internal.dispatcher.DefaultExceptionHandleDispatcher;
import com.example.lib.common.starter.internal.strategy.HandledExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.SystemExceptionStrategy;
import com.example.lib.common.starter.internal.strategy.UnhandledExceptionStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

/**
 * 공통 예외 처리 모듈(common-starter)의 자동 구성(AutoConfiguration) 클래스
 * AOP 컴포넌트, 예외 디스패처, 기본 처리 전략 등의 빈(Bean)을 컨텍스트에 등록합니다.
 */
@AutoConfiguration
public class CommonStarterConfig {

    @Bean
    public ThrowsAspect throwsAspect() {
        return new ThrowsAspect();
    }

    @Bean
    public GlobalExceptionWrapperAspect globalExceptionTranslatorAspect() {
        return new GlobalExceptionWrapperAspect();
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionHandleDispatcher.class)
    public ExceptionHandleDispatcher exceptionHandleDispatcher(
        List<ExceptionHandleStrategy> strategies,
        ExceptionContextTracer tracer) {
        return new DefaultExceptionHandleDispatcher(strategies, tracer);
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
