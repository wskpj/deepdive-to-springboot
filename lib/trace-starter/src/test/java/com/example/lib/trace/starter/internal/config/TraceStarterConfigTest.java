package com.example.lib.trace.starter.internal.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import com.example.lib.trace.core.TraceIdGenerator;
import com.example.lib.trace.core.handler.ExceptionContextTracer;
import com.example.lib.trace.starter.internal.aspect.TraceAspect;
import com.example.lib.trace.starter.internal.decorator.AsyncTraceDecorator;

class TraceStarterConfigTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TraceStarterConfig.class));

    @Test
    @DisplayName("TraceIdGenerator 빈이 등록되어야 한다")
    void shouldRegisterTraceIdGenerator() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(TraceIdGenerator.class);
        });
    }

    @Test
    @DisplayName("TraceFilter 빈이 등록되어야 한다")
    void shouldRegisterTraceFilter() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasBean("requestTraceFilter");
        });
    }

    @Test
    @DisplayName("AsyncTraceDecorator 빈이 등록되어야 한다")
    void shouldRegisterAsyncTraceDecorator() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(AsyncTraceDecorator.class);
        });
    }

    @Test
    @DisplayName("TraceAspect 빈이 등록되어야 한다")
    void shouldRegisterTraceAspect() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(TraceAspect.class);
        });
    }

    @Test
    @DisplayName("ExceptionContextTracer 빈이 등록되어야 한다")
    void shouldRegisterExceptionContextTracer() {
        contextRunner.run(context -> {
            if (context.getStartupFailure() != null) {
                context.getStartupFailure().printStackTrace();
            }
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(ExceptionContextTracer.class);
        });
    }
}
