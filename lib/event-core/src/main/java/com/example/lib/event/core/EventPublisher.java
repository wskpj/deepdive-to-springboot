package com.example.lib.event.core;

public interface EventPublisher {

    void publish(BaseEvent event);

    void publishAsync(BaseEvent event);
}
