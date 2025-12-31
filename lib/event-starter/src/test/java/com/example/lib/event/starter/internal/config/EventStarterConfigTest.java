package com.example.lib.event.starter.internal.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.example.lib.event.core.EventPublisher;

class EventStarterConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(EventStarterConfig.class));

    @Test
    @DisplayName("EventPublisher 빈이 정상적으로 등록되어야 한다")
    void shouldRegisterEventPublisher() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(EventPublisher.class);
        });
    }
}
