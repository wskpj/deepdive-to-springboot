package com.example.deepdive_app.global.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogFilter implements Filter {

    private static final String TRACE_ID = "traceId";
    private static final String REQUEST_URI = "uri";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String uri = req.getRequestURI();
        String method = req.getMethod();

        long startTime = System.currentTimeMillis();

        try {
            MDC.put(TRACE_ID, traceId);
            MDC.put(REQUEST_URI, uri);

            log.info(">>> [REQUEST] {} {}", method, uri);

            chain.doFilter(request, response);

        } finally {
            long duration = System.currentTimeMillis() - startTime;

            log.info("<<< [RESPONSE] {} {} ({}ms)", method, uri, duration);

            MDC.clear();
        }
    }
}
