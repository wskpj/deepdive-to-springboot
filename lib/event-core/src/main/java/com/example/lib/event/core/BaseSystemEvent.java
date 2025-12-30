package com.example.lib.event.core;

import lombok.Getter;

@Getter
public abstract class BaseSystemEvent extends BaseEvent {

    protected BaseSystemEvent(EventType eventType, EventSource eventSource) {
        super(eventType, eventSource);
    }
}
