package com.example.lib.debug.starter.internal.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class DebugStarterConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DebugStarterConfig.class));

    @Test
    void shouldRegisterDebugStarterConfig() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(DebugStarterConfig.class);
        });
    }
}
