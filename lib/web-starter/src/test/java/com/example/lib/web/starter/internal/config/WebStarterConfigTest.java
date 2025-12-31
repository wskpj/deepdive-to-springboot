package com.example.lib.web.starter.internal.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.example.lib.common.starter.internal.config.CommonStarterConfig;
import com.example.lib.web.core.dispatcher.ExceptionResponseDispatcher;
import com.example.lib.web.core.mapper.ExceptionResponseMapper;
import com.example.lib.web.starter.internal.handler.StandardExceptionHandler;

class WebStarterConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    JacksonAutoConfiguration.class,
                    CommonStarterConfig.class
            ))
            .withUserConfiguration(WebStarterConfig.class);

    @Test
    @DisplayName("ExceptionResponseDispatcher 빈이 정상적으로 등록되어야 한다")
    void shouldRegisterExceptionResponseDispatcher() {
        contextRunner.run(context -> {
            if (context.getStartupFailure() != null) {
                context.getStartupFailure().printStackTrace();
            }
            assertThat(context).hasSingleBean(ExceptionResponseDispatcher.class);
        });
    }

    @Test
    @DisplayName("StandardExceptionHandler 빈이 정상적으로 등록되어야 한다")
    void shouldRegisterStandardExceptionHandler() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(StandardExceptionHandler.class);
        });
    }

    @Test
    @DisplayName("기본 응답 매퍼들이 모두 등록되어야 한다")
    void shouldRegisterDefaultMappers() {
        int MAPPER_COUNT = 5;

        contextRunner.run(context -> {
            assertThat(context).getBeans(ExceptionResponseMapper.class).hasSizeGreaterThanOrEqualTo(MAPPER_COUNT);
        });
    }
}
