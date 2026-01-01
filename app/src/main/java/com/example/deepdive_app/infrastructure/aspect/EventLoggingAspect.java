package com.example.deepdive_app.infrastructure.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.example.lib.event.core.BaseEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class EventLoggingAspect {

    @Pointcut("execution(* com.example.lib.event.core.pub.EventPublisher+.publish(..)) && args(event)")
    public void eventPublishPointcut(BaseEvent event) {}

    @Before(value = "eventPublishPointcut(event)", argNames = "event")
    public void logEventPublish(BaseEvent event) {
        log.debug("[{}] Type: {}, ID: {}", this.getClass().getSimpleName(), event.getEventType(), event.getEventId());
    }
}
