package com.example.lib.common.core.exception;

import java.util.Map;

public record ExceptionOrigin(
    String className,
    String methodName,
    int lineNumber,
    Map<String, Object> arguments
) {
}
