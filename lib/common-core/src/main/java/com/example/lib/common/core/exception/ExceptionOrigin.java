package com.example.lib.common.core.exception;

import java.util.Map;

/**
 * BaseException에 포함되어 예외 발생 지점을 추적하는 레코드
 */
public record ExceptionOrigin(
    String className,
    String methodName,
    int lineNumber,
    Map<String, Object> arguments
) {
}
