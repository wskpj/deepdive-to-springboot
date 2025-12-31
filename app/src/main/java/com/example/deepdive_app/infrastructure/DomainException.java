package com.example.deepdive_app.infrastructure;

import com.example.lib.common.core.exception.HandledException;

public class DomainException extends HandledException {
    public DomainException(AppErrorType errorType) {
        super(errorType, null);
    }

    public DomainException(AppErrorType errorType, Object details) {
        super(errorType, details);
    }
}
