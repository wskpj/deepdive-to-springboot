package com.example.lib.web.starter.internal.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.lib.web.starter.internal.properties.WebProperties;

import lombok.RequiredArgsConstructor;

@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnProperty(prefix = "web.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebCorsConfig {

    private final WebProperties webProperties;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOriginPatterns(webProperties.allowedOrigins());
        config.setAllowedMethods(webProperties.allowedMethods());
        config.setAllowedHeaders(webProperties.allowedHeaders());
        config.setAllowCredentials(webProperties.allowCredentials());
        config.setMaxAge(webProperties.maxAge());

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
