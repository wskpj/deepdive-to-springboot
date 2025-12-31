package com.example.lib.web.core;

import org.springframework.http.HttpStatus;

import com.example.lib.common.core.ErrorType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebError implements ErrorType {

    // 4xx Client Errors
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "W400", "Bad Request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "W401", "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "W403", "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "W404", "Not Found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "W405", "Method Not Allowed"),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "W406", "Not Acceptable"),
    CONFLICT(HttpStatus.CONFLICT, "W409", "Conflict"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "W415", "Unsupported Media Type"),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "W422", "Unprocessable Entity"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "W429", "Too Many Requests"),

    // 5xx Server Errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "W500", "Internal Server Error"),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "W502", "Bad Gateway"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "W503", "Service Unavailable"),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "W504", "Gateway Timeout");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public int getStatus() {
        return httpStatus.value();
    }
}