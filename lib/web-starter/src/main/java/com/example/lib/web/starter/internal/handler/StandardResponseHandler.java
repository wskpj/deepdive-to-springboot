package com.example.lib.web.starter.internal.handler;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.lib.trace.core.TraceConstants;
import com.example.lib.web.core.dto.ApiResult;
import com.example.lib.web.starter.internal.filter.ResponseFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestControllerAdvice
@RequiredArgsConstructor
@SuppressWarnings("null")
public class StandardResponseHandler implements ResponseBodyAdvice<Object> {

    private final ResponseFilter responseFilter;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();

        if (ApiResult.class.isAssignableFrom(parameterType)) {
            return false;
        }

        if (Resource.class.isAssignableFrom(parameterType) || byte[].class.isAssignableFrom(parameterType)) {
            return false;
        }

        return responseFilter.supports(returnType);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType,
                                  Class<? extends HttpMessageConverter<?>> converterType, 
                                  ServerHttpRequest request, ServerHttpResponse response) {

        String traceId = MDC.get(TraceConstants.TRACE_ID);
        ApiResult<Object> wrappedBody = ApiResult.ok(body, traceId);

        // StringHttpMessageConverter가 선택된 경우 수동으로 JSON 문자열 변환
        if (StringHttpMessageConverter.class.isAssignableFrom(converterType)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return objectMapper.writeValueAsString(wrappedBody);
        }

        return wrappedBody;
    }
}
