package com.example.lib.common.core.exception;

public interface ErrorType {

    String getCode();

    String getMessage();

    default ErrorLevel getLogLevel() {
        return ErrorLevel.ERROR;
    }
}
