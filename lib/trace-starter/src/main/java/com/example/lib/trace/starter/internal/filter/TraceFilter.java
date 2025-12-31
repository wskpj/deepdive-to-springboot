package com.example.lib.trace.starter.internal.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

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
public class TraceFilter extends OncePerRequestFilter {

    private final TraceIdGenerator traceIdGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

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

            updateRequestContext(wrappedRequest);

            filterChain.doFilter(wrappedRequest, response);
        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            MDC.put(TraceConstants.STATUS, String.valueOf(status));
            MDC.put(TraceConstants.ELAPSED_TIME, String.valueOf(elapsedTime)); // ms
            log.info("{} {} {} - {}ms", status, method, uri, elapsedTime);

            MDC.clear();
        }
    }

    private void updateRequestContext(ContentCachingRequestWrapper wrappedRequest) {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(wrappedRequest, attributes.getResponse()), true);
        }
    }
}
