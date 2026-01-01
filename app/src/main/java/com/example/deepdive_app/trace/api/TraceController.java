package com.example.deepdive_app.trace.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.deepdive_app.infrastructure.AppEventType;
import com.example.deepdive_app.trace.event.TraceEvent;
import com.example.deepdive_app.trace.service.TraceService;
import com.example.lib.event.core.EventPublisher;
import com.example.lib.trace.core.Trace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Trace
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TraceController {

    private final TraceService traceService;
    private final EventPublisher eventPublisher;

    @Trace
    @GetMapping("/sync")
    public String sync() {
        traceService.syncMethod();
        return "Sync OK";
    }

    @GetMapping("/async")
    public String async() {
        traceService.asyncMethod();
        return "Async request triggered";
    }

    @GetMapping("/event")
    public String triggerEvent() {
        eventPublisher.publish(TraceEvent.of(AppEventType.TRACE_EVENT));
        return "Event published";
    }
}
