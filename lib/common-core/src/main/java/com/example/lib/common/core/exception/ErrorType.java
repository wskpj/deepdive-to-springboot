package com.example.lib.common.core.exception;

/**
 * BaseException에 포함되는 에러 타입 인터페이스
 */
public interface ErrorType {

    String getCode();

    String getMessage();

    default ErrorLevel getLevel() {
        return ErrorLevel.ERROR;
    }
}
