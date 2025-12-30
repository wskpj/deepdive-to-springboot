package com.example.lib.event.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import com.example.lib.event.core.EventPublisher;
import com.example.lib.event.starter.internal.DefaultEventPublisher;

@AutoConfiguration
public class EventStarterConfig {

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultEventPublisher(applicationEventPublisher);
    }
}
