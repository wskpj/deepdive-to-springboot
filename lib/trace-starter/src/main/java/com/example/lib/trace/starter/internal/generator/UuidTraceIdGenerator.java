package com.example.lib.trace.starter.internal.generator;

import java.util.UUID;

import com.example.lib.trace.core.TraceIdGenerator;

public class UuidTraceIdGenerator implements TraceIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
