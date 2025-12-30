package com.example.deepdive_app.trace.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TraceService {
    
    @SneakyThrows
    public void syncMethod() {
        log.info("Start Synchronous Method");
        Thread.sleep(1000);
        log.info("Finish Synchronous Method");
    }

    @Async
    @SneakyThrows
    public void asyncMethod() {
        log.info("Start Asynchronous Method");
        Thread.sleep(1000);
        log.info("Finish Asynchronous Method");
    }
}
