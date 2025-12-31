package com.example.lib.trace.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.example.lib.trace.core.ExceptionContextTracer;
import com.example.lib.trace.core.TraceIdGenerator;
import com.example.lib.trace.starter.internal.aspect.AsyncExecutionAspect;
import com.example.lib.trace.starter.internal.aspect.ExceptionTraceAspect;
import com.example.lib.trace.starter.internal.decorator.AsyncTraceDecorator;
import com.example.lib.trace.starter.internal.filter.TraceFilter;
import com.example.lib.trace.starter.internal.generator.UuidTraceIdGenerator;
import com.example.lib.trace.starter.internal.handler.DefaultExceptionContextTracer;

import jakarta.servlet.http.HttpServletRequest;

@AutoConfiguration
@SuppressWarnings("null")
public class TraceStarterConfig {

    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilterRegistration(TraceIdGenerator traceIdGenerator) {
        FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceFilter(traceIdGenerator));
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
    public AsyncExecutionAspect asyncExecutionAspect() {
        return new AsyncExecutionAspect();
    }

    @Bean
    public ExceptionTraceAspect exceptionTraceAspect(ExceptionContextTracer tracer) {
        return new ExceptionTraceAspect(tracer);
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
