package com.example.lib.event.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import com.example.lib.event.core.EventPublisher;
import com.example.lib.event.starter.internal.DefaultEventPublisher;

/**
 * 이벤트 모듈(event-starter)의 자동 구성(AutoConfiguration) 클래스
 * 기본 EventPublisher 구현체 등의 빈을 등록합니다.
 */
@AutoConfiguration
public class EventStarterConfig {

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public EventPublisher eventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultEventPublisher(applicationEventPublisher);
    }
}
