package com.example.lib.trace.core;

/**
 * Interface for logging execution traces with hierarchy and duration.
 */
public interface LogTrace {
    TraceStatus begin(String message);
    TraceStatus begin(String message, TraceLevel level);
    void end(TraceStatus status);
    void end(TraceStatus status, String extraInfo);
    void destroy();
}
