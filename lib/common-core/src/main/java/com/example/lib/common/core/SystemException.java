package com.example.lib.common.core;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SystemException extends RuntimeException {

    protected final ErrorType errorType;
    protected final Object details;
}
