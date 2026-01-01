package com.example.lib.event.starter.internal;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;

import com.example.lib.event.core.BaseEvent;
import com.example.lib.event.core.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
