package com.example.lib.event.starter.internal;

import com.example.lib.event.core.EventSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultEventSource implements EventSource {

    DEFAULT_SOURCE("DEFAULT_SOURCE");

    private final String source;
}
