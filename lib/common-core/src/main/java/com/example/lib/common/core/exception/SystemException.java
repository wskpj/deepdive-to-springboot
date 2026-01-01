package com.example.lib.common.core.exception;

import lombok.Getter;

@Getter
public non-sealed class SystemException extends BaseException {

    public SystemException() {
        super();
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(ErrorType errorType) {
        super(errorType);
    }

    public SystemException(ErrorType errorType, Object details) {
        super(errorType, details);
    }

    public SystemException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public SystemException(ErrorType errorType, Object details, Throwable cause) {
        super(errorType, details, cause);
    }
}
