package com.example.deepdive_app.debug;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lib.common.core.exception.HandledException;
import com.example.lib.common.core.exception.SystemException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class debugController {
  
    @GetMapping("/exception/handled")
    public void triggerHandledException() {
        throw new HandledException();
    }

    @GetMapping("/exception/system")
    public void triggerSystemException() {
        throw new SystemException();
    }

    @GetMapping("/exception/unhandled")
    public void triggerUnhandledException() {
        throw new RuntimeException();  
    }
}
