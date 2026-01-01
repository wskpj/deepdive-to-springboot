package com.example.lib.common.core.exception;

import lombok.Getter;

/**
 * 예상된 비즈니스 로직 예외
 * 애플리케이션에서 비즈니스 예외로 상속해서 사용하거나, HandledException 생성자를 직접 호출해서 사용
 */
@Getter
public non-sealed class HandledException extends BaseException {

    public HandledException() {
        this(null, null, null);
    }

    public HandledException(Throwable cause) {
        this(null, null, cause);
    }

    public HandledException(ErrorType errorType) {
        this(errorType, null, null);
    }

    public HandledException(ErrorType errorType, Object details) {
        this(errorType, details, null);
    }

    public HandledException(ErrorType errorType, Throwable cause) {
        this(errorType, null, cause);
    }

    public HandledException(ErrorType errorType, Object details, Throwable cause) {
        super(errorType == null ? new DefaultHandledError() : errorType, details, cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    private static class DefaultHandledError implements ErrorType {

        @Override
        public String getCode() {
            return "UNKNOWN_HANDLED_ERROR";
        }

        @Override
        public String getMessage() {
            return "Unknown Handled Error";
        }

        @Override
        public ErrorLevel getLogLevel() {
            return ErrorLevel.WARN;
        }
    }
}
