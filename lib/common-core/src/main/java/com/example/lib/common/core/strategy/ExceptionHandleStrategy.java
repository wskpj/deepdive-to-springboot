package com.example.lib.common.core.strategy;

public interface ExceptionHandleStrategy {

    boolean supports(Exception e);

    void handle(Exception e);
}
