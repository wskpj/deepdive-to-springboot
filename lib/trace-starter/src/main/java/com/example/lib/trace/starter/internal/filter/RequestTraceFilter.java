package com.example.lib.trace.starter.internal.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.example.lib.common.core.context.LocalContext;
import com.example.lib.trace.core.TraceConstants;
import com.example.lib.trace.core.TraceIdGenerator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("null")
public class RequestTraceFilter extends OncePerRequestFilter {

    private final TraceIdGenerator traceIdGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest wrappedRequest = request;
        if (isCacheable(request)) {
            wrappedRequest = new ContentCachingRequestWrapper(request);
        }

        String traceId = traceIdGenerator.generate();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString() == null ? "{}" : request.getQueryString();
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        long startTime = System.currentTimeMillis();

        try {
            MDC.put(TraceConstants.TRACE_ID, traceId);
            MDC.put(TraceConstants.REQUEST_URI, uri);
            MDC.put(TraceConstants.METHOD, method);
            MDC.put(TraceConstants.PARAMS, params);
            MDC.put(TraceConstants.CLIENT_IP, clientIp);
            MDC.put(TraceConstants.USER_AGENT, userAgent);

            filterChain.doFilter(wrappedRequest, response);
        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            MDC.put(TraceConstants.STATUS, String.valueOf(status));
            MDC.put(TraceConstants.ELAPSED_TIME, String.valueOf(elapsedTime));
            log.info("{} {} {} - {}ms from {}", status, method, uri, elapsedTime, clientIp);

            LocalContext.clear();
            MDC.clear();
        }
    }

    private boolean isCacheable(HttpServletRequest request) {
        String method = request.getMethod();
        String contentType = request.getContentType();

        // Skip multipart (file uploads) to avoid OOM
        if (contentType != null && contentType.toLowerCase().contains("multipart/form-data")) {
            return false;
        }

        return "POST".equalsIgnoreCase(method) ||
               "PUT".equalsIgnoreCase(method) ||
               "PATCH".equalsIgnoreCase(method);
    }
}
