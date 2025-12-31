package com.example.deepdive_app.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppEventSource implements com.example.lib.event.core.EventSource {
    
    TRACE("TRACE"),
    AUTH("AUTH");
    
    private final String source;
}
