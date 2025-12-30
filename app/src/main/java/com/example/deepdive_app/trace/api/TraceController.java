package com.example.deepdive_app.trace.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.deepdive_app.infrastructure.AppEventType;
import com.example.deepdive_app.trace.event.TraceEvent;
import com.example.deepdive_app.trace.service.TraceService;
import com.example.lib.event.core.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TraceController {

    private final TraceService traceService;
    private final EventPublisher eventPublisher;

    @GetMapping("/sync")
    public String sync() {
        log.info("Controller: Received sync request");
        traceService.syncMethod();
        return "Sync OK";
    }

    @GetMapping("/async")
    public String async() {
        log.info("Controller: Received async request");
        traceService.asyncMethod();
        return "Async request triggered";
    }

    @GetMapping("/event")
    public String triggerEvent() {
        log.info("Controller: Publishing event");
        eventPublisher.publish(TraceEvent.of(AppEventType.TRACE_EVENT));
        return "Event published";
    }
}
