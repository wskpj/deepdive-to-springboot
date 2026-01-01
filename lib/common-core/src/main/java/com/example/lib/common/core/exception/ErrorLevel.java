package com.example.lib.common.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 수준
 * 예외 처리, 로깅 & 트레이싱 등에 활용
 */
@Getter
@RequiredArgsConstructor
public enum ErrorLevel {
    INFO(false, false),
    WARN(false, false),
    ERROR(true, true);

    private final boolean shouldCollectStackTrace;
    private final boolean shouldCollectPayload;
}
