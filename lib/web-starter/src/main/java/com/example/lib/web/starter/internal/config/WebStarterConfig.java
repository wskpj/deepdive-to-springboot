package com.example.lib.web.starter.internal.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.lib.common.core.ExceptionHandleDispatcher;
import com.example.lib.common.core.ExceptionHandleStrategy;
import com.example.lib.web.core.ExceptionResponseDispatcher;
import com.example.lib.web.core.ExceptionResponseMapper;
import com.example.lib.web.starter.internal.dispatcher.DefaultExceptionResponseDispatcher;
import com.example.lib.web.starter.internal.filter.ResponseFilter;
import com.example.lib.web.starter.internal.handler.StandardExceptionHandler;
import com.example.lib.web.starter.internal.handler.StandardResponseHandler;
import com.example.lib.web.starter.internal.mapper.HandledExceptionResponseMapper;
import com.example.lib.web.starter.internal.mapper.SystemExceptionResponseMapper;
import com.example.lib.web.starter.internal.mapper.UnhandledExceptionResponseMapper;
import com.example.lib.web.starter.internal.mapper.ValidationExceptionResponseMapper;
import com.example.lib.web.starter.internal.properties.WebProperties;
import com.example.lib.web.starter.internal.strategy.ValidationExceptionStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebStarterConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean(ResponseFilter.class)
    public ResponseFilter responseFilter(WebProperties webProperties) {
        return ResponseFilter.ofPrefixes(webProperties.responseFilterPrefixes().toArray(String[]::new));
    }

    @Bean
    @ConditionalOnMissingBean(name = "standardResponseHandler")
    public StandardResponseHandler standardResponseHandler(
            ResponseFilter responseFilter,
            ObjectMapper objectMapper) {
        return new StandardResponseHandler(responseFilter, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(name = "standardExceptionHandler")
    public StandardExceptionHandler standardExceptionHandler(
            ExceptionHandleDispatcher handleDispatcher,
            ExceptionResponseDispatcher responseDispatcher) {
        return new StandardExceptionHandler(handleDispatcher, responseDispatcher);
    }

    @Bean
    @ConditionalOnMissingBean(name = "exceptionResponseDispatcher")
    public ExceptionResponseDispatcher exceptionResponseDispatcher(
            List<ExceptionResponseMapper> mappers) {
        return new DefaultExceptionResponseDispatcher(mappers);
    }

    /**
     * Mappers
     */
    @Bean
    @ConditionalOnMissingBean(name = "handledExceptionResponseMapper")
    public ExceptionResponseMapper handledExceptionResponseMapper() {
        return new HandledExceptionResponseMapper();
    }

    @Bean
    @ConditionalOnMissingBean(name = "unhandledExceptionResponseMapper")
    public ExceptionResponseMapper unhandledExceptionResponseMapper() {
        return new UnhandledExceptionResponseMapper();
    }

    @Bean
    @ConditionalOnMissingBean(name = "systemExceptionResponseMapper")
    public ExceptionResponseMapper systemExceptionResponseMapper() {
        return new SystemExceptionResponseMapper();
    }

    @Bean
    @ConditionalOnMissingBean(name = "validationExceptionResponseMapper")
    public ExceptionResponseMapper defaultValidationExceptionResponseMapper() {
        return new ValidationExceptionResponseMapper();
    }

    /**
     * Strategies
     */
    @Bean
    @ConditionalOnMissingBean(name = "validationExceptionStrategy")
    public ExceptionHandleStrategy validationExceptionStrategy() {
        return new ValidationExceptionStrategy();
    }
}
