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

/**
 * HTTP 요청의 시작과 끝을 감지하여 LogTrace로 기록하고,
 * MDC에 HTTP 표준 메타데이터(URI, Method, IP 등)를 초기화하는 서블릿 필터.
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("null")
public class RequestTraceFilter extends OncePerRequestFilter {

    private final LogTrace logTrace;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Cacheable 요청인 경우 바디를 여러 번 읽을 수 있도록 래핑
        HttpServletRequest wrappedRequest = request;
        if (isCacheable(request)) {
            wrappedRequest = new ContentCachingRequestWrapper(request);
        }

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
        
        // 2. MDC에 기본 HTTP 요청 정보(Metadata) 설정
        MDC.put(TraceConstants.REQUEST_URI, uri);
        MDC.put(TraceConstants.METHOD, method);
        MDC.put(TraceConstants.CLIENT_IP, clientIp);
        MDC.put(TraceConstants.USER_AGENT, request.getHeader("User-Agent"));
        MDC.put(TraceConstants.PARAMS, request.getQueryString() == null ? "{}" : request.getQueryString());

        TraceStatus status = null;
        try {
            // 3. 요청 처리에 대한 로그 트레이스 시작
            String message = String.format("%s %s from %s", method, uri, clientIp);
            status = logTrace.begin(message);

            // 4. 다음 필터/서블릿으로 처리 위임
            filterChain.doFilter(wrappedRequest, response);
        } finally {
            // 5. 응답 상태 코드와 함께 트레이스 종료 및 전체 컨텍스트 초기화
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
