package com.example.lib.common.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandledExceptionTest {

    @Test
    @DisplayName("기본 생성자는 Unknown Handled Error 정보를 기본값으로 가진다")
    void defaultConstructor() {
        HandledException ex = new HandledException();
        
        assertAll(
            () -> assertEquals("UNKNOWN_HANDLED_ERROR", ex.getErrorType().getCode()),
            () -> assertEquals(ErrorLevel.WARN, ex.getLogLevel()),
            () -> assertNull(ex.getDetails()),
            () -> assertNull(ex.getCause())
        );
    }

    @Test
    @DisplayName("모든 인자 생성자에 null을 넣어도 기본값으로 안전하게 초기화된다")
    void allArgsConstructorWithNulls() {
        HandledException ex = new HandledException(null, null, null);
        
        assertAll(
            () -> assertEquals("UNKNOWN_HANDLED_ERROR", ex.getErrorType().getCode(), "ErrorType이 null이면 기본 에러타입이 설정되어야 함"),
            () -> assertNull(ex.getDetails(), "details가 null이면 null이어야 함"),
            () -> assertNull(ex.getCause(), "cause가 null이면 null이어야 함")
        );
    }

    @Test
    @DisplayName("모든 인자 생성자가 모든 값을 올바르게 필드에 매핑한다")
    void allArgsConstructorWithFullValues() {
        ErrorType errorType = new ErrorType() {
            @Override public String getCode() { return "H400"; }
            @Override public String getMessage() { return "Bad Request"; }
            @Override public ErrorLevel getLogLevel() { return ErrorLevel.INFO; }
        };
        String details = "detail info";
        RuntimeException cause = new RuntimeException("root");

        HandledException ex = new HandledException(errorType, details, cause);
        
        assertAll(
            () -> assertEquals("H400", ex.getErrorType().getCode()),
            () -> assertEquals(details, ex.getDetails()),
            () -> assertEquals(cause, ex.getCause()),
            () -> assertEquals(ErrorLevel.INFO, ex.getLogLevel())
        );
    }

    @Test
    @DisplayName("HandledException은 최적화를 위해 스택 트레이스를 비워둔다")
    void fillInStackTrace() {
        HandledException ex = new HandledException();
        assertEquals(0, ex.getStackTrace().length);
    }
}
