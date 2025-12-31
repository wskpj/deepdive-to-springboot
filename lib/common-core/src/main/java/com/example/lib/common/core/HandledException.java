package com.example.lib.common.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HandledException extends RuntimeException {

    protected final ErrorType errorType;
    protected final Object details;
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
