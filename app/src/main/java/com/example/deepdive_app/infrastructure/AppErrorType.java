package com.example.deepdive_app.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppErrorType implements com.example.lib.common.core.exception.ErrorType {
    
    MEMBER_EMAIL_EXISTING("MEMBER_EMAIL_EXISTING", "Email already exists"),
    MEMBER_CREATION_FAILED("MEMBER_CREATION_FAILED", "Member creation failed");
    
    private final String code;
    private final String message;
}
