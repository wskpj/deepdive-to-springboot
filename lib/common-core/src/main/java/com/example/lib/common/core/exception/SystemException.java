package com.example.lib.common.core.exception;

import lombok.Getter;

/**
 * 예상된 시스템 예외
 * 애플리케이션에서 시스템 예외로 상속해서 사용하거나, SystemException 생성자를 직접 호출해서 사용
 */
@Getter
public class SystemException extends HandledException {

    public SystemException() {
        this(null, null, null);
    }

    public SystemException(ErrorType errorType) {
        this(errorType, null, null);
    }

    public SystemException(Throwable cause) {
        this(null, null, cause);
    }

    public SystemException(ErrorType errorType, Throwable cause) {
        this(errorType, null, cause);
    }

    public SystemException(ErrorType errorType, Object details, Throwable cause) {
        // SystemException은 원인 예외를 받을 수 있으며, 스택트레이스를 활성화합니다.
        super(errorType == null ? new UnknownSystemError() : errorType, details, cause, true, true);
    }

    private static class UnknownSystemError implements ErrorType {

        private static final String UNKNOWN_SYSTEM_ERROR_CODE = "UNKNOWN_SYSTEM_ERROR";
        private static final String UNKNOWN_SYSTEM_ERROR_MESSAGE = "Unknown System Error";

        @Override
        public String getCode() {
            return UNKNOWN_SYSTEM_ERROR_CODE;
        }

        @Override
        public String getMessage() {
            return UNKNOWN_SYSTEM_ERROR_MESSAGE;
        }

        @Override
        public ErrorLevel getLevel() {
            return ErrorLevel.ERROR;
        }
    }
}
