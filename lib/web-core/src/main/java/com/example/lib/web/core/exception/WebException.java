package com.example.lib.web.core.exception;

import com.example.lib.common.core.exception.HandledException;

/**
 * 웹 계층에서 발생하는 모든 예외의 최상위 추상 클래스
 * HTTP 상태 코드와 연결된 WebError를 필수적으로 가짐
 */
public abstract class WebException extends HandledException {

    protected WebException(WebError webError) {
        super(webError);
    }
}
