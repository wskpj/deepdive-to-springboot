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
import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.ErrorLevel;
import com.example.lib.common.core.exception.ExceptionOrigin;
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
        collectExceptionOrigin(e);

        ErrorLevel level = (e instanceof BaseException be) ? be.getLogLevel() : ErrorLevel.ERROR;

        if (level.isShouldCollectStackTrace()) {
            collectStackTrace(e);
        }
        if (level.isShouldCollectPayload()) {
            collectPayload();
        }
    }

    private void collectExceptionOrigin(Exception e) {
        if (e instanceof BaseException be && be.getOrigin() != null) {
            ExceptionOrigin origin = be.getOrigin();
            LocalContext.put(TraceConstants.EXCEPTION_ORIGIN, origin.toString());
            log.debug("[{}] Trace Origin: {}:{} {}()", this.getClass().getSimpleName(), 
                origin.className(), origin.lineNumber(), origin.methodName());
        } else {
            // Standard Exception / No Origin yet: Search StackTrace
            for (StackTraceElement ste : e.getStackTrace()) {
                String cn = ste.getClassName();
                if (cn.startsWith("com.example") && !cn.endsWith("Exception") && !cn.contains("BaseException")) {
                    log.debug("[{}] Trace Origin (Estimated): {}:{} {}()", this.getClass().getSimpleName(),
                            cn, ste.getLineNumber(), ste.getMethodName());
                    LocalContext.put(TraceConstants.EXCEPTION_ORIGIN, String.format("%s:%d %s()", cn, ste.getLineNumber(), ste.getMethodName()));
                    break;
                }
            }
        }
    }

    private void collectStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        LocalContext.put(TraceConstants.STACK_TRACE, sw.toString());
    }

    private void collectPayload() {
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