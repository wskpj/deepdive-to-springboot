package com.example.deepdive_app.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TraceEventListener {

    /**
     * 동기 이벤트 리스너: 발행한 스레드와 동일한 스레드에서 실행됨
     */
    @EventListener
    @SneakyThrows
    public void handleSyncEvent(TraceEvent event) {
        log.info("Caught Sync Event: {}", event.getMessage());
        Thread.sleep(1000);
        log.info("Handling Sync Event: {}", event.getMessage());
    }
    
    /**
     * 비동기 이벤트 리스너: 별도의 비동기 스레드에서 실행됨
     * MdcTaskDecorator에 의해 부모의 TraceID가 전파되어야 함
    */
   @Async
   @EventListener
   @SneakyThrows
   public void handleAsyncEvent(TraceEvent event) {
        log.info("Caught Async Event: {}", event.getMessage());
        Thread.sleep(2000);
        log.info("Handling Async Event: {}", event.getMessage());
    }
}
