package com.example.lib.common.core;

public interface ExceptionHandleStrategy {

    boolean supports(Exception e);

    void handle(Exception e);
}
