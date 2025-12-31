package com.example.lib.trace.starter.internal.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import com.example.lib.common.core.HandledException;
import com.example.lib.common.core.SystemException;
import com.example.lib.trace.core.ExceptionContextTracer;
import com.example.lib.trace.core.TraceConstants;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DefaultExceptionContextTracer implements ExceptionContextTracer {

    private final HttpServletRequest request;

    public void handle(Exception e) {
        injectPayload();

        MDC.put(TraceConstants.EXCEPTION_CLASS, e.getClass().getName());
        MDC.put(TraceConstants.EXCEPTION_MESSAGE, e.getMessage());

        switch (e) {
            // Handled Exception (WARN)
            case HandledException he -> {
                // Not Contains Stacktace
            }

            // System Exception (ERROR)
            case SystemException se -> {
                // Contains Stacktrace
                MDC.put(TraceConstants.STACK_TRACE, parseStackTrace(se));
            }

            // Unhandled Exception (FATAL)
            case Exception ex -> {
                // Contains Stacktrace
                MDC.put(TraceConstants.STACK_TRACE, parseStackTrace(ex));
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
        MDC.put(TraceConstants.HEADERS, headers);

        // Contains Request Params
        String params = extraceParams();
        MDC.put(TraceConstants.PARAMS, params);

        // Contains Request Body
        String payload = extractPayload();
        MDC.put(TraceConstants.PAYLOAD, payload);

        if (log.isDebugEnabled()){
            log.debug("[Headers] {}", headers);
            log.debug("[Params] {}", params);
            log.debug("[Payload] {}", payload);
        }
    }

    private String extractHeaders() {
        Enumeration<String> names = request.getHeaderNames();

        if (names != null) {
            Map<String, String> map = new HashMap<>();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                if (name.equalsIgnoreCase("cookie") || name.equalsIgnoreCase("authorization")) continue;
                map.put(name, request.getHeader(name));
            }
            return map.toString();
        }

        return "{}";
    }

    private String extraceParams() {
        String params = request.getQueryString();
        return params == null ? "{}" : params;
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
        HttpServletRequest currentRequest = (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)
                ? attributes.getRequest() : request;

        return WebUtils.getNativeRequest(currentRequest, ContentCachingRequestWrapper.class);
    }
}