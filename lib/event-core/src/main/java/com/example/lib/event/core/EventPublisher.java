package com.example.lib.event.core;

/**
 * 애플리케이션 내에서 이벤트를 발행하는 인터페이스
 * 동기 및 비동기 방식으로 이벤트를 발행할 수 있는 기능을 제공합니다.
 */
public interface EventPublisher {

    void publish(BaseEvent event);

    void publishAsync(BaseEvent event);
}
