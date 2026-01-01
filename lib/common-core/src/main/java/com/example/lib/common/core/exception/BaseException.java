package com.example.lib.common.core.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public sealed class BaseException extends RuntimeException permits HandledException, SystemException {

    protected final ErrorType errorType;
    protected final Object details;

    @Setter
    private ExceptionOrigin origin;

    protected BaseException(Throwable cause) {
        this(new ErrorType() {
            @Override
            public String getCode() {
                return "UNKNOWN_ERROR";
            }

            @Override
            public String getMessage() {
                return "Unknown Error";
            }
        }, null, cause);
    }

    protected BaseException(ErrorType errorType) {
        this(errorType, null, null);
    }

    protected BaseException(ErrorType errorType, Object details) {
        this(errorType, details, null);
    }

    protected BaseException(ErrorType errorType, Throwable cause) {
        this(errorType, null, cause);
    }

    protected BaseException(ErrorType errorType, Object details, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
        this.details = details;
    }
}
