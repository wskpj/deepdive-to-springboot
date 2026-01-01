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

/**
 * 예외 발생 시 현재 스레드의 HTTP 요청 정보와 예외 발생 지점(Origin)을 수집하여
 * MDCInstantLogger를 통해 즉시 로깅하는 기본 추적기
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("null")
public class DefaultExceptionContextTracer implements ExceptionContextTracer {

    private final HttpServletRequest request;

    @Override
    public void handle(Exception e) {
        log.debug("[{}:>>] Starting Tracing Exception Context", this.getClass().getSimpleName());

        // 1. 추적에 사용할 임시 로컬 컨텍스트 생성 (메서드 종료 후 GC됨)
        Map<String, Object> context = new HashMap<>();
        
        context.put(TraceConstants.EXCEPTION_CLASS, e.getClass().getSimpleName());
        context.put(TraceConstants.EXCEPTION_MESSAGE, e.getMessage());
        
        // 2. 클래스, 메시지 및 발생 지점(Origin) 정보 수집
        collectExceptionOrigin(e, context);

        ErrorLevel level = (e instanceof BaseException be) ? be.getLogLevel() : ErrorLevel.ERROR;

        // 3. 예외 레벨에 따라 스택 트레이스 및 Payload 선택적 수집
        if (level.isShouldCollectStackTrace()) {
            collectStackTrace(e, context);
        }
        if (level.isShouldCollectPayload()) {
            collectPayload(context);
        }

        String errorCode = (e instanceof BaseException be) ? be.getErrorType().getCode() : "SYSTEM_ERROR";
        String message = String.format("[%s] %s", e.getClass().getSimpleName(), errorCode);

        // 4. 예외 로그 레벨에 맞춰 MDC 즉시 로깅 실행
        switch (level) {
            case INFO -> MDCInstantLogger.info(message, context);
            case WARN -> MDCInstantLogger.warn(message, context);
            case ERROR -> MDCInstantLogger.error(message, context);
        }

        log.debug("[{}:<<] Finished Tracing Exception Context : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
    }

    private void collectExceptionOrigin(Exception e, Map<String, Object> context) {
        // 1. 예외가 Origin을 이미 가지고 있다면 해당 정보 사용
        if (e instanceof BaseException be && be.getOrigin() != null) {
            ExceptionOrigin origin = be.getOrigin();
            context.put(TraceConstants.EXCEPTION_ORIGIN, origin.toString());
            log.debug("[{}:--] Trace Origin: {}:{} {}()", this.getClass().getSimpleName(),
                    origin.className(),
                    origin.lineNumber(),
                    origin.methodName());
        } else {
            // 2. Origin이 없다면 스택 트레이스를 뒤져서 'com.example' 패키지 내의 최초 발생 지점 추정
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