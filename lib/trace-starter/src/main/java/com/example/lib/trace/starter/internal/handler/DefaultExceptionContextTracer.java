package com.example.lib.trace.starter.internal.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import com.example.lib.common.core.context.LocalContext;
import com.example.lib.common.core.exception.HandledException;
import com.example.lib.common.core.exception.SystemException;
import com.example.lib.trace.core.ExceptionContextTracer;
import com.example.lib.trace.core.TraceConstants;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("null")
public class DefaultExceptionContextTracer implements ExceptionContextTracer {

    private final HttpServletRequest request;

    @Override
    public void handle(Exception e) {
        log.debug("[{}] Tracing Exception Context : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());

        // Collect and store context data in LocalContext (instead of MDC)
        LocalContext.put(TraceConstants.EXCEPTION_CLASS, e.getClass().getSimpleName());
        LocalContext.put(TraceConstants.EXCEPTION_MESSAGE, e.getMessage());

        switch (e) {
            // Handled Exception (WARN)
            case HandledException he -> {
                // Not Contains Stacktrace
            }

            // System Exception (ERROR)
            case SystemException se -> {
                // Contains Stacktrace
                injectPayload();
                LocalContext.put(TraceConstants.STACK_TRACE, parseStackTrace(se));
            }

            // Unhandled Exception (FATAL)
            case Exception ex -> {
                // Contains Stacktrace
                injectPayload();
                LocalContext.put(TraceConstants.STACK_TRACE, parseStackTrace(ex));
            }
        }

    }

    private String parseStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private void injectPayload() {
        // Contains Request Headers
        String headers = extractHeaders();
        LocalContext.put(TraceConstants.HEADERS, headers);
        log.debug("[{}] Headers: {}", this.getClass().getSimpleName(), headers);

        // Contains Request Payload (Body)
        String payload = extractPayload();
        LocalContext.put(TraceConstants.PAYLOAD, payload);
        log.debug("[{}] Payload: {}", this.getClass().getSimpleName(), payload);
    }

    private String extractHeaders() {
        Enumeration<String> names = request.getHeaderNames();

        if (names != null) {
            Map<String, String> map = new HashMap<>();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                if (name.equalsIgnoreCase("cookie") || name.equalsIgnoreCase("authorization"))
                    continue;
                map.put(name, request.getHeader(name));
            }
            return map.toString();
        }

        return "{}";
    }

    private String extractPayload() {
        ContentCachingRequestWrapper wrapped = getWrapper();
        if (wrapped != null) {
            byte[] body = wrapped.getContentAsByteArray();
            if (body.length > 0) {
                return new String(body, StandardCharsets.UTF_8);
            }
        }
        return "{}";
    }

    private ContentCachingRequestWrapper getWrapper() {
        HttpServletRequest currentRequest = (RequestContextHolder
                .getRequestAttributes() instanceof ServletRequestAttributes attributes)
                        ? attributes.getRequest()
                        : request;

        return WebUtils.getNativeRequest(currentRequest, ContentCachingRequestWrapper.class);
    }
}