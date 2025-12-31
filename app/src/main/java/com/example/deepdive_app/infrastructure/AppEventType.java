package com.example.deepdive_app.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppEventType implements com.example.lib.event.core.EventType {
    
    TRACE_EVENT("TRACE_EVENT", "Trace Event"),
    MEMBER_SIGNED_UP("MEMBER_SIGNED_UP", "Member Signed Up");
    
    private final String code;
    private final String message;
}
