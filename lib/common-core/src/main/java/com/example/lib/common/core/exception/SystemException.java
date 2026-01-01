package com.example.lib.common.core.exception;

import lombok.Getter;

@Getter
public non-sealed class SystemException extends BaseException {

    public SystemException() {
        this(null, null, null);
    }

    public SystemException(Throwable cause) {
        this(null, null, cause);
    }

    public SystemException(ErrorType errorType) {
        this(errorType, null, null);
    }

    public SystemException(ErrorType errorType, Object details) {
        this(errorType, details, null);
    }

    public SystemException(ErrorType errorType, Throwable cause) {
        this(errorType, null, cause);
    }

    public SystemException(ErrorType errorType, Object details, Throwable cause) {
        super(errorType == null ? new DefaultSystemException() : errorType, details, cause);
    }

    private static class DefaultSystemException implements ErrorType {

        @Override
        public String getCode() {
            return "UNKNOWN_SYSTEM_ERROR";
        }

        @Override
        public String getMessage() {
            return "Unknown System Error";
        }

        @Override
        public ErrorLevel getLogLevel() {
            return ErrorLevel.ERROR;
        }
    }
}
