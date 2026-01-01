package com.example.lib.trace.starter.internal.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.example.lib.common.core.context.LocalContext;
import com.example.lib.trace.core.LogTrace;
import com.example.lib.trace.core.TraceConstants;
import com.example.lib.trace.core.TraceStatus;

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

    private final LogTrace logTrace;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest wrappedRequest = request;
        if (isCacheable(request)) {
            wrappedRequest = new ContentCachingRequestWrapper(request);
        }

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
        
        // Metadata for MDC (standard fields)
        MDC.put(TraceConstants.REQUEST_URI, uri);
        MDC.put(TraceConstants.METHOD, method);
        MDC.put(TraceConstants.CLIENT_IP, clientIp);
        MDC.put(TraceConstants.USER_AGENT, request.getHeader("User-Agent"));
        MDC.put(TraceConstants.PARAMS, request.getQueryString() == null ? "{}" : request.getQueryString());

        TraceStatus status = null;
        try {
            String message = String.format("%s %s from %s", method, uri, clientIp);
            status = logTrace.begin(message);

            filterChain.doFilter(wrappedRequest, response);
        } finally {
            if (status != null) {
                int httpStatus = response.getStatus();
                MDC.put(TraceConstants.STATUS, String.valueOf(httpStatus));
                logTrace.end(status, String.format("[%d]", httpStatus));
            }
            
            logTrace.destroy();
            LocalContext.clear();
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
