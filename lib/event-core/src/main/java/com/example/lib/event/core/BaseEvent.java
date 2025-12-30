package com.example.lib.event.core;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class BaseEvent {

    protected final UUID eventId;
    protected final EventType eventType;
    protected final EventSource eventSource;
    protected final LocalDateTime timestamp;

    protected BaseEvent(EventType eventType, EventSource eventSource) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.timestamp = LocalDateTime.now();
    }
}
