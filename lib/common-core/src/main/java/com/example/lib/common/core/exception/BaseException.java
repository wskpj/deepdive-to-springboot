package com.example.lib.common.core.exception;

import lombok.Getter;

/**
 * 모든 커스텀 예외의 상위 클래스
 * 인스턴스를 생성할 수 없고, HandledException과 UnhandledException만 상속하도록 제한함
 */
@Getter
public sealed class BaseException extends RuntimeException permits HandledException, UnhandledException {

    protected final ErrorType errorType;
    protected final Object details;

    protected BaseException(ErrorType errorType, Object details, Throwable cause) {
        this(errorType, details, cause, true, true);
    }

    protected BaseException(ErrorType errorType, Object details, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorType == null ? UnknownError.UNKNOWN_ERROR_MESSAGE : errorType.getMessage(), cause, enableSuppression, writableStackTrace);
        this.errorType = errorType == null ? new UnknownError() : errorType;
        this.details = details;
    }

    public ErrorLevel getLogLevel() {
        return errorType.getLevel();
    }

    private static class UnknownError implements ErrorType {

        private static final String UNKNOWN_ERROR_CODE = "UNKNOWN_ERROR";
        private static final String UNKNOWN_ERROR_MESSAGE = "Unknown Error";

        @Override
        public String getCode() {
            return UNKNOWN_ERROR_CODE;
        }

        @Override
        public String getMessage() {
            return UNKNOWN_ERROR_MESSAGE;
        }

        @Override
        public ErrorLevel getLevel() {
            return ErrorLevel.ERROR;
        }
    }
}
