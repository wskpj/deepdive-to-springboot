package com.example.lib.web.core.exception;

import org.springframework.http.HttpStatus;

import com.example.lib.common.core.exception.ErrorLevel;
import com.example.lib.common.core.exception.ErrorType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 웹 계층에서 발생하는 표준 HTTP 에러 타입
 * HTTP 상태 코드와 메시지, 기본 로그 레벨을 함께 정의
 */
@Getter
@RequiredArgsConstructor
public enum WebError implements ErrorType {

    // 4xx Client Errors (INFO)
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "W400", "Bad Request", ErrorLevel.INFO),
    NOT_FOUND(HttpStatus.NOT_FOUND, "W404", "Not Found", ErrorLevel.INFO),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "W405", "Method Not Allowed", ErrorLevel.INFO),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "W406", "Not Acceptable", ErrorLevel.INFO),
    CONFLICT(HttpStatus.CONFLICT, "W409", "Conflict", ErrorLevel.INFO),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "W415", "Unsupported Media Type", ErrorLevel.INFO),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "W422", "Unprocessable Entity", ErrorLevel.INFO),

    // 4xx Client Erros (WARN)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "W401", "Unauthorized", ErrorLevel.WARN),
    FORBIDDEN(HttpStatus.FORBIDDEN, "W403", "Forbidden", ErrorLevel.WARN),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "W429", "Too Many Requests", ErrorLevel.WARN),

    // 5xx Server Errors (ERROR)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "W500", "Internal Server Error", ErrorLevel.ERROR),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "W502", "Bad Gateway", ErrorLevel.ERROR),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "W503", "Service Unavailable", ErrorLevel.ERROR),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "W504", "Gateway Timeout", ErrorLevel.ERROR);

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final ErrorLevel level;

    public int getStatus() {
        return httpStatus.value();
    }
}
