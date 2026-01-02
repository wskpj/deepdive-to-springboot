package com.example.lib.common.core.exception;

import lombok.Getter;

/**
 * 예상하지 못한 예외
 * 애플리케이션에서 발생한 Exception을 래핑하는 데 사용
 */
@Getter
public final class UnhandledException extends BaseException {

    public UnhandledException(Throwable cause) {
        super(new UnhandledError(), null, cause, true, true);
        if (cause == null) {
            throw new IllegalArgumentException("UnhandledException must have a cause");
        }
    }

    private static class UnhandledError implements ErrorType {

        private static final String UNHANDLED_ERROR_CODE = "UNHANDLED_ERROR";
        private static final String UNHANDLED_ERROR_MESSAGE = "Unhandled Error";

        @Override
        public String getCode() {
            return UNHANDLED_ERROR_CODE;
        }

        @Override
        public String getMessage() {
            return UNHANDLED_ERROR_MESSAGE;
        }

        @Override
        public ErrorLevel getLevel() {
            return ErrorLevel.ERROR;
        }
    }
}
