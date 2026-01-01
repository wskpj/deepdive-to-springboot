package com.example.lib.event.core;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

/**
 * 시스템 내에서 발생하는 모든 이벤트(도메인 이벤트, 시스템 이벤트 등)의 최상위 추상 클래스
 * 고유 식별자(ID), 타입, 출처 및 발생 시간 등의 메타데이터를 공통으로 제공합니다.
 */
@Getter
public abstract class BaseEvent {

    protected final UUID eventId;
    protected final EventType eventType;
    protected final EventSource eventSource;
    protected final LocalDateTime timestamp;

    protected BaseEvent(EventType eventType, EventSource eventSource) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.timestamp = LocalDateTime.now();
    }
}
