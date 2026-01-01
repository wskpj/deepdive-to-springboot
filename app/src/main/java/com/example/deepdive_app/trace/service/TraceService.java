package com.example.deepdive_app.trace.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.lib.trace.core.Trace;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Trace
@Slf4j
@Service
public class TraceService {
    
    @SneakyThrows
    public void syncMethod() {
        Thread.sleep(1000);
    }

    @Async
    @SneakyThrows
    public void asyncMethod() {
        Thread.sleep(1000);
    }
}
