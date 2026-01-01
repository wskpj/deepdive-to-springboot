package com.example.lib.common.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 모든 커스텀 예외의 상위 클래스
 * 컴파일 시점에서 HandledException과 SystemException만 상속받도록 제한함
 */
@Getter
public sealed class BaseException extends RuntimeException permits HandledException, SystemException {

    protected final ErrorType errorType;
    protected final Object details;

    @Setter
    private ExceptionOrigin origin;

    protected BaseException() {
        this(new UnknownException(), null, null);
    }

    protected BaseException(Throwable cause) {
        this(new UnknownException(), null, cause);
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
        super(errorType == null ? "Unknown Error" : errorType.getMessage(), cause);
        this.errorType = errorType == null ? new UnknownException() : errorType;
        this.details = details;
    }

    public ErrorLevel getLogLevel() {
        return errorType.getLogLevel();
    }

    private static class UnknownException implements ErrorType {
        @Override
        public String getCode() {
            return "UNKNOWN_ERROR";
        }

        @Override
        public String getMessage() {
            return "Unknown Error";
        }

        @Override
        public ErrorLevel getLogLevel() {
            return ErrorLevel.ERROR;
        }
    }
}
