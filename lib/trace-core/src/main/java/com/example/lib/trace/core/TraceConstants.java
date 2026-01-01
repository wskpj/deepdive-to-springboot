package com.example.lib.trace.core;

public final class TraceConstants {    
    // Info
    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";
    public static final String PARENT_SPAN_ID = "parentSpanId";
    public static final String TOTAL_ELAPSED_TIME = "totalElapsedTime";
    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String DEPTH = "depth";

    public static final String STATUS = "status";
    public static final String METHOD = "method";
    public static final String REQUEST_URI = "uri";
    public static final String PARAMS = "params";

    public static final String CLIENT_IP = "clientIp";
    public static final String USER_AGENT = "userAgent";

    // Debug
    public static final String EXCEPTION_CLASS = "exceptionClass";
    public static final String EXCEPTION_MESSAGE = "exceptionMessage";
    public static final String STACK_TRACE = "stackTrace";

    public static final String HEADERS = "headers";
    public static final String PAYLOAD = "payload";

    private TraceConstants() {}
}
