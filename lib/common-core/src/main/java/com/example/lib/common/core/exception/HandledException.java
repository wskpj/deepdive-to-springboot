package com.example.lib.common.core.exception;

import lombok.Getter;

/**
 * 예상된 예외
 * 애플리케이션에서 비즈니스 예외로 상속해서 사용하거나, HandledException 생성자를 직접 호출해서 사용
 */
@Getter
public non-sealed class HandledException extends BaseException {

    public HandledException() {
        this(null, null, null);
    }

    public HandledException(ErrorType errorType) {
        this(errorType, null, null);
    }

    public HandledException(Throwable cause) {
        this(null, null, cause);
    }

    public HandledException(ErrorType errorType, Object details, Throwable cause) {
        super(errorType == null ? new UnknownHandledError() : errorType, details, cause, true, false);
    }

    protected HandledException(ErrorType errorType, Object details, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        // SystemException 등에서 스택트레이스를 활성화하기 위해 열어둔 protected 생성자
        super(errorType == null ? new UnknownHandledError() : errorType, details, cause, enableSuppression, writableStackTrace);
    }

    private static class UnknownHandledError implements ErrorType {

        private static final String UNKNOWN_HANDLED_ERROR_CODE = "UNKNOWN_HANDLED_ERROR";
        private static final String UNKNOWN_HANDLED_ERROR_MESSAGE = "Unknown Handled Error";

        @Override
        public String getCode() {
            return UNKNOWN_HANDLED_ERROR_CODE;
        }

        @Override
        public String getMessage() {
            return UNKNOWN_HANDLED_ERROR_MESSAGE;
        }

        @Override
        public ErrorLevel getLevel() {
            return ErrorLevel.WARN;
        }
    }
}
