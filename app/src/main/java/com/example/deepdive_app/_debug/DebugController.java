package com.example.deepdive_app._debug;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lib.common.core.annotation.Throws;
import com.example.lib.common.core.exception.HandledException;
import com.example.lib.common.core.exception.SystemException;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("null")
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {

    @GetMapping("/success")
    public String triggerSuccess() {
        return "success";
    }

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

    @Throws(HandledException.class)
    @GetMapping("/throws/handled")
    public void triggerThrowsHandledException() {
        try {
            String nullStr = null;
            nullStr.length();
        } catch (Exception e) {
            throw e;
        }
    }

    @Throws(SystemException.class)
    @GetMapping("/throws/system")
    public void triggerThrowsSystemException() {
        try {
            String nullStr = null;
            nullStr.length();
        } catch (Exception e) {
            throw e;
        }
    }
}
