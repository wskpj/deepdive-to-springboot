package com.example.lib.trace.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.example.lib.trace.core.ExceptionContextTracer;
import com.example.lib.trace.core.LogTrace;
import com.example.lib.trace.core.TraceIdGenerator;
import com.example.lib.trace.starter.internal.aspect.TraceAspect;
import com.example.lib.trace.starter.internal.decorator.AsyncTraceDecorator;
import com.example.lib.trace.starter.internal.filter.RequestTraceFilter;
import com.example.lib.trace.starter.internal.generator.UuidTraceIdGenerator;
import com.example.lib.trace.starter.internal.handler.DefaultExceptionContextTracer;
import com.example.lib.trace.starter.internal.log.ThreadLocalLogTrace;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 트레이스 모듈(trace-starter)의 자동 구성(AutoConfiguration) 클래스
 * HTTP 필터, 비동기 데코레이터, LogTrace, 예외 컨텍스트 추적기 등 추적과 관련된 빈을 등록합니다.
 */
@AutoConfiguration
@SuppressWarnings("null")
public class TraceStarterConfig {

    @Bean
    public FilterRegistrationBean<RequestTraceFilter> requestTraceFilter(LogTrace logTrace) {
        FilterRegistrationBean<RequestTraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestTraceFilter(logTrace));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // first order
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(AsyncTraceDecorator.class)
    public AsyncTraceDecorator asyncTraceDecorator() {
        return new AsyncTraceDecorator();
    }

    @Bean
    @ConditionalOnMissingBean
    public LogTrace logTrace(TraceIdGenerator traceIdGenerator) {
        return new ThreadLocalLogTrace(traceIdGenerator);
    }

    @Bean
    public TraceAspect traceAspect(LogTrace logTrace) {
        return new TraceAspect(logTrace);
    }

    @Bean
    public ThreadPoolTaskExecutorCustomizer mdcTaskExecutorCustomizer(AsyncTraceDecorator asyncTraceDecorator) {
        return executor -> executor.setTaskDecorator(asyncTraceDecorator);
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionContextTracer.class)
    public ExceptionContextTracer exceptionContextHandler(HttpServletRequest request) {
        return new DefaultExceptionContextTracer(request);
    }

    @Bean
    @ConditionalOnMissingBean(TraceIdGenerator.class)
    public TraceIdGenerator traceIdGenerator() {
        return new UuidTraceIdGenerator();
    }
}
