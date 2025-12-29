package com.example.deepdive_app.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 트레이싱 테스트를 위한 샘플 이벤트 객체
 */
@Getter
@RequiredArgsConstructor
public class TraceEvent {
    private final String message;
}
