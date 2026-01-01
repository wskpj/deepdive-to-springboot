package com.example.lib.event.starter.internal;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;

import com.example.lib.event.core.BaseEvent;
import com.example.lib.event.core.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 스프링의 ApplicationEventPublisher를 사용하여 이벤트를 발행하는 기본 구현체
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(BaseEvent event) {
        log.debug("[{}] Publish Event Type: {}, ID: {}", this.getClass().getSimpleName(), event.getEventType(), event.getEventId());
        applicationEventPublisher.publishEvent(event);
    }
    
    @Async
    public void publishAsync(BaseEvent event) {
        log.debug("[{}] Publish Async Event Type: {}, ID: {}", this.getClass().getSimpleName(), event.getEventType(), event.getEventId());
        applicationEventPublisher.publishEvent(event);
    }
}
