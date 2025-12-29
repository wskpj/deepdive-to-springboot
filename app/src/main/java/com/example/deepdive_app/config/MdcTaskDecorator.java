package com.example.deepdive_app.config;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import lombok.extern.slf4j.Slf4j;

/**
 * 비동기 스레드 실행 시 부모 스레드의 MDC 컨텍스트를 복사하여 전달하는 데코레이터
 */
@Slf4j
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 시각적 구분을 위해 indent 설정
        String parentIndent = MDC.get("indent") != null ? MDC.get("indent") : "";
        String childIndent = parentIndent + "|--- ";

        // [부모 스레드] MDC 컨텍스트 복사
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // [자식 스레드] 복사한 컨텍스트를 현재 스레드(비동기 스레드)에 주입
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                
                // [자식 스레드] 깊어진 인덴트 적용
                MDC.put("indent", childIndent);
                
                log.info("task thread start: {}", Thread.currentThread().getName());
                runnable.run();
                log.info("task thread end: {}", Thread.currentThread().getName());
            } finally {
                // [자식 스레드 종료] 스레드 풀 오염 방지를 위해 비움
                MDC.clear();
            }
        };
    }
}
