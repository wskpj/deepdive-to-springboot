package com.example.lib.redis.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.lib.redis.starter.internal.operator.StandardRedisListOperator;
import com.example.lib.redis.starter.internal.operator.StandardRedisSetOperator;
import com.example.lib.redis.starter.internal.operator.StandardRedisStringOperator;
import com.example.lib.redis.starter.internal.script.StandardRedisScriptExecutor;

@AutoConfiguration
public class RedisStarterConfig {

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardRedisStringOperator redisStringOperator(RedisTemplate<String, Object> redisTemplate) {
        return new StandardRedisStringOperator(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardRedisListOperator redisListOperator(RedisTemplate<String, Object> redisTemplate) {
        return new StandardRedisListOperator(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardRedisSetOperator redisSetOperator(RedisTemplate<String, Object> redisTemplate) {
        return new StandardRedisSetOperator(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardRedisScriptExecutor redisScriptExecutor(RedisTemplate<String, Object> redisTemplate) {
        return new StandardRedisScriptExecutor(redisTemplate);
    }
}
