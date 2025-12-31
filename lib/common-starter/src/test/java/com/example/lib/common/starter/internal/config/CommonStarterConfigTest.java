package com.example.lib.common.starter.internal.config;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.example.lib.common.core.dispatcher.ExceptionHandleDispatcher;
import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.trace.core.ExceptionContextTracer;

class CommonStarterConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CommonStarterConfig.class))
            .withBean(ExceptionContextTracer.class, () -> mock(ExceptionContextTracer.class));

    @Test
    @DisplayName("ExceptionHandleDispatcher 빈이 정상적으로 등록되어야 한다")
    void shouldRegisterExceptionHandleDispatcher() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ExceptionHandleDispatcher.class);
        });
    }

    @Test
    @DisplayName("기본 예외 처리 전략들이 모두 등록되어야 한다")
    void shouldRegisterDefaultStrategies() {
        int STRATEGY_COUNT = 3;

        contextRunner.run(context -> {
            assertThat(context).getBeans(ExceptionHandleStrategy.class).hasSizeGreaterThanOrEqualTo(STRATEGY_COUNT);
        });
    }
}
