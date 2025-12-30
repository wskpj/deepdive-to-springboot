package com.example.deepdive_app.infrastructure;

import com.example.lib.event.core.EventSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppEventSource implements EventSource {
    
    TRACE("TRACE"),
    AUTH("AUTH");
    
    private final String source;
}
