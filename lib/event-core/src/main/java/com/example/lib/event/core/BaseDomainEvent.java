package com.example.lib.event.core;

import lombok.Getter;

@Getter
public abstract class BaseDomainEvent extends BaseEvent {

    protected BaseDomainEvent(EventType eventType, EventSource eventSource) {
        super(eventType, eventSource);
    }
}
