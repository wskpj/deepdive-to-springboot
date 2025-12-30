package com.example.deepdive_app.infrastructure;

import com.example.lib.event.core.EventType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppEventType implements EventType {
    
    TRACE_EVENT("TRACE_EVENT", "Trace Event");
    
    private final String code;
    private final String message;
}
