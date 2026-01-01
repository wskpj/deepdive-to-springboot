package com.example.lib.common.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemExceptionTest {

    @Test
    @DisplayName("기본 생성자는 Unknown System Error 정보를 기본값으로 가진다")
    void defaultConstructor() {
        SystemException ex = new SystemException();
        
        assertAll(
            () -> assertEquals("UNKNOWN_SYSTEM_ERROR", ex.getErrorType().getCode()),
            () -> assertEquals(ErrorLevel.ERROR, ex.getLogLevel()),
            () -> assertNull(ex.getDetails())
        );
    }

    @Test
    @DisplayName("모든 인자 생성자에 ErrorType이 null이면 System전용 기본 에러타입이 설정된다")
    void allArgsConstructorWithNullErrorType() {
        // ErrorType에 null 전달
        SystemException ex = new SystemException(null, "some details", null);
        
        assertAll(
            () -> assertNotNull(ex.getErrorType(), "ErrorType은 절대 null일 수 없음"),
            () -> assertEquals("UNKNOWN_SYSTEM_ERROR", ex.getErrorType().getCode(), "SystemException 전용 기본 코드가 설정되어야 함"),
            () -> assertEquals(ErrorLevel.ERROR, ex.getLogLevel(), "SystemException의 기본 레벨은 ERROR여야 함")
        );
    }

    @Test
    @DisplayName("모든 인자 생성자가 모든 값을 올바르게 필드에 매핑한다")
    void allArgsConstructorWithFullValues() {
        ErrorType errorType = new ErrorType() {
            @Override public String getCode() { return "CRITICAL_500"; }
            @Override public String getMessage() { return "Critical System Failure"; }
            @Override public ErrorLevel getLogLevel() { return ErrorLevel.ERROR; }
        };
        String details = "DB Connection Timeout";
        RuntimeException cause = new RuntimeException("DB down");

        SystemException ex = new SystemException(errorType, details, cause);
        
        assertAll(
            () -> assertEquals("CRITICAL_500", ex.getErrorType().getCode()),
            () -> assertEquals(details, ex.getDetails()),
            () -> assertEquals(cause, ex.getCause())
        );
    }

    @Test
    @DisplayName("SystemException은 추적을 위해 스택 트레이스를 생성해야 한다")
    void fillInStackTrace() {
        SystemException ex = new SystemException();
        assertTrue(ex.getStackTrace().length > 0, "SystemException should have a stack trace for debugging");
    }
}
