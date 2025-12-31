package com.example.lib.common.core.exception;

import lombok.Getter;

@Getter
public non-sealed class HandledException extends BaseException {

    public HandledException(Throwable cause) {
        super(cause);
    }

    public HandledException(ErrorType errorType) {
        super(errorType);
    }

    public HandledException(ErrorType errorType, Object details) {
        super(errorType, details, null);
    }

    public HandledException(ErrorType errorType, Throwable cause) {
        super(errorType, null, cause);
    }

    public HandledException(ErrorType errorType, Object details, Throwable cause) {
        super(errorType, details, cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
