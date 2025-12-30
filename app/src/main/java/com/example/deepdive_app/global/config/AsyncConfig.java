package com.example.deepdive_app.global.config;

import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * ThreadPoolTaskExecutor 타입 빈에 MdcTaskDecorator를 설정하는 커스터마이저
     */
    @Bean
    public ThreadPoolTaskExecutorCustomizer mdcTaskExcutorCustomier() {
        return executor -> executor.setTaskDecorator(new MdcTaskDecorator());
    }
}
