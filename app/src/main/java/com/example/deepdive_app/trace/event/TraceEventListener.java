package com.example.deepdive_app.trace.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TraceEventListener {

    @EventListener
    @SneakyThrows
    public void handleSyncEvent(TraceEvent event) {
        log.debug("Caught Sync Event: {}", event.getEventType());
        Thread.sleep(1000);
    }

   @Async
   @EventListener
   @SneakyThrows
   public void handleAsyncEvent(TraceEvent event) {
        log.debug("Caught Async Event: {}", event.getEventType());
        Thread.sleep(2000);
    }
}
