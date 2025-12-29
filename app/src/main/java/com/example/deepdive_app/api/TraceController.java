package com.example.deepdive_app.api;

import com.example.deepdive_app.service.TraceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TraceController {

    private final TraceService traceService;

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
}
