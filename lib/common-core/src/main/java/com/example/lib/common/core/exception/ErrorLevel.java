package com.example.lib.common.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorLevel {
    DEBUG(false, false),
    INFO(false, false),
    WARN(false, false),
    ERROR(true, true);

    private final boolean shouldCollectStackTrace;
    private final boolean shouldCollectPayload;
}
