package com.example.lib.web.starter.internal.strategy;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.example.lib.common.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.core.exception.WebError;
import com.example.lib.web.core.exception.WebException;

import lombok.extern.slf4j.Slf4j;

/**
 * 웹 계층에서 명시적으로 발생시킨 WebException 처리 전략
 * 인증/인가 예외나 요청 한도 초과 등은 WARN 레벨로, 그 외 일반 오류는 INFO 레벨로 로깅함
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 50)
public class WebExceptionStrategy implements ExceptionHandleStrategy {

    @Override
    public boolean supports(Exception e) {
        return e instanceof WebException;
    }

    @Override
    public void handle(Exception e) {
        WebException we = (WebException) e;

        // 1. 에러 타입(WebError)에 따라 로그 레벨 분기 처리
        switch (we.getErrorType()) {
            case WebError.UNAUTHORIZED, WebError.FORBIDDEN, WebError.TOO_MANY_REQUESTS:
                // 2. 인증/인가 실패나 과도한 요청(429) 등은 WARN으로 남겨 모니터링 가능하게 함
                log.warn("[Web Exception] {}", we.getErrorType().getMessage());
                break;
            default:
                // 3. 그 외 일반적인 클라이언트 요청 오류(400, 404 등)는 단순 INFO 로깅
                log.info("[Web Exception] {}", we.getErrorType().getMessage());
                break;
        }
    }
}
