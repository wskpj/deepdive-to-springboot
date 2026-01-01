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

import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.ErrorLevel;
import com.example.lib.common.core.exception.ExceptionOrigin;
import com.example.lib.trace.core.ExceptionContextTracer;
import com.example.lib.trace.core.TraceConstants;
import com.example.lib.trace.starter.internal.log.MDCInstantLogger;

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
        log.debug("[{}:>>] Starting Tracing Exception Context", this.getClass().getSimpleName());

        // Collect everything into a LOCAL map (will be GC'd after this method)
        Map<String, Object> context = new HashMap<>();
        
        context.put(TraceConstants.EXCEPTION_CLASS, e.getClass().getSimpleName());
        context.put(TraceConstants.EXCEPTION_MESSAGE, e.getMessage());
        
        collectExceptionOrigin(e, context);

        ErrorLevel level = (e instanceof BaseException be) ? be.getLogLevel() : ErrorLevel.ERROR;

        if (level.isShouldCollectStackTrace()) {
            collectStackTrace(e, context);
        }
        if (level.isShouldCollectPayload()) {
            collectPayload(context);
        }

        // Determine log level and message
        String errorCode = (e instanceof BaseException be) ? be.getErrorType().getCode() : "SYSTEM_ERROR";
        String message = String.format("[%s] %s", e.getClass().getSimpleName(), errorCode);

        // Log INSTANTLY with context
        switch (level) {
            case INFO -> MDCInstantLogger.info(message, context);
            case WARN -> MDCInstantLogger.warn(message, context);
            case ERROR -> MDCInstantLogger.error(message, context);
        }

        log.debug("[{}:<<] Finished Tracing Exception Context : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
    }

    private void collectExceptionOrigin(Exception e, Map<String, Object> context) {
        if (e instanceof BaseException be && be.getOrigin() != null) {
            ExceptionOrigin origin = be.getOrigin();
            context.put(TraceConstants.EXCEPTION_ORIGIN, origin.toString());
            log.debug("[{}:--] Trace Origin: {}:{} {}()", this.getClass().getSimpleName(),
                    origin.className(),
                    origin.lineNumber(),
                    origin.methodName());
        } else {
            // Standard Exception / No Origin yet: Search StackTrace
            for (StackTraceElement ste : e.getStackTrace()) {
                String cn = ste.getClassName();
                if (cn.startsWith("com.example") && !cn.endsWith("Exception") && !cn.contains("BaseException")) {
                    String originStr = String.format("%s:%d %s()", cn, ste.getLineNumber(), ste.getMethodName());
                    context.put(TraceConstants.EXCEPTION_ORIGIN, originStr);
                    log.debug("[{}:--] Trace Origin (Estimated): {}:{} {}()", this.getClass().getSimpleName(),
                            cn,
                            ste.getLineNumber(),
                            ste.getMethodName());
                    break;
                }
            }
        }
    }

    private void collectStackTrace(Exception e, Map<String, Object> context) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        context.put(TraceConstants.STACK_TRACE, sw.toString());
    }

    private void collectPayload(Map<String, Object> context) {
        String headers = extractHeaders();
        context.put(TraceConstants.HEADERS, extractHeaders());
        log.debug("[{}] Headers: {}", this.getClass().getSimpleName(), headers);
        String payload = extractPayload();
        context.put(TraceConstants.PAYLOAD, extractPayload());
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