package com.example.lib.redis.starter.internal.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.lib.redis.starter.internal.operator.StandardRedisListOperator;
import com.example.lib.redis.starter.internal.operator.StandardRedisSetOperator;
import com.example.lib.redis.starter.internal.operator.StandardRedisStringOperator;
import com.example.lib.redis.starter.internal.script.StandardRedisScriptExecutor;

class RedisStarterConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(RedisStarterConfig.class))
            .withUserConfiguration(TestConfig.class);

    @Test
    void shouldRegisterRedisBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RedisTemplate.class);
            assertThat(context).hasSingleBean(StandardRedisStringOperator.class);
            assertThat(context).hasSingleBean(StandardRedisListOperator.class);
            assertThat(context).hasSingleBean(StandardRedisSetOperator.class);
            assertThat(context).hasSingleBean(StandardRedisScriptExecutor.class);
        });
    }

    @Configuration
    static class TestConfig {
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return mock(RedisConnectionFactory.class);
        }
    }
}
