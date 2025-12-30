package com.example.deepdive_app.trace.event;

import com.example.deepdive_app.infrastructure.AppEventSource;
import com.example.lib.event.core.BaseEvent;
import com.example.lib.event.core.EventType;

import lombok.Getter;

@Getter
public class TraceEvent extends BaseEvent {

    public TraceEvent(EventType eventType) {
        super(eventType, AppEventSource.TRACE);
    }

    public static TraceEvent of(EventType type) {
        return new TraceEvent(type);
    }
}
