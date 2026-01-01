package com.example.deepdive_app.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppErrorType implements com.example.lib.common.core.exception.ErrorType {

    MEMBER_CREATION_FAILED("MEMBER_CREATION_FAILED", "Member creation failed"),
    AUTH_SIGNUP_FAILED("AUTH_SIGNUP_FAILED", "Signup failed");

    private final String code;
    private final String message;
}
