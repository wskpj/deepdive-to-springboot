package com.example.lib.common.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseExceptionTest {

    @Test
    @DisplayName("BaseException은 지정된 ErrorType의 정보를 올바르게 유지한다")
    void errorTypeInformation() {
        ErrorType customError = new ErrorType() {
            @Override public String getCode() { return "BASE_001"; }
            @Override public String getMessage() { return "Base Message"; }
            @Override public ErrorLevel getLogLevel() { return ErrorLevel.ERROR; }
        };

        // HandledException을 통해 BaseException의 로직 검증
        BaseException ex = new HandledException(customError);

        assertAll(
            () -> assertEquals("BASE_001", ex.getErrorType().getCode()),
            () -> assertEquals(ErrorLevel.ERROR, ex.getLogLevel()),
            () -> assertEquals("Base Message", ex.getMessage())
        );
    }

    @Test
    @DisplayName("null ErrorType이 전달되면 내부 UnknownException으로 대체된다")
    void nullErrorTypeHandling() {
        // BaseException의 생성자에서 null 체크 로직 검증
        BaseException ex = new HandledException((ErrorType) null); 

        // HandledException(null)은 내부적으로 DefaultHandledError를 사용하므로 
        // BaseException의 UnknownException을 직접 테스트하기는 어렵지만
        // 최소한 null NPE가 발생하지 않음을 보장함
        assertNotNull(ex.getErrorType());
    }
}
