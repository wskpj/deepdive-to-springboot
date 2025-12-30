package com.example.lib.event.starter.internal;

import com.example.lib.event.core.EventType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultEventType implements EventType {

    DEFAULT_EVENT("DEFAULT_EVENT", "Default Event");

    private final String code;
    private final String description;
}
