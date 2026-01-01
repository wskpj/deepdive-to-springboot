package com.example.lib.web.starter.internal.handler;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.lib.trace.core.TraceConstants;
import com.example.lib.web.core.dto.ApiError;
import com.example.lib.web.core.dto.ApiResult;
import com.example.lib.web.starter.internal.filter.ResponseFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 스프링 MVC 컨트롤러의 정상 및 예외 응답을 전역적으로 가로채어
 * ApiResult 표준 포맷으로 래핑(Wrapping)하는 어드바이스(ResponseBodyAdvice)
 */
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
        // 1. 현재 요청의 Trace ID 조회
        String traceId = MDC.get(TraceConstants.TRACE_ID);
        ApiResult<Object> wrappedBody;

        if (body instanceof ApiError error) {
            // 2. 예외(ApiError) 응답인 경우 HTTP 상태 코드를 세팅하고 실패(fail) 포맷으로 래핑
            response.setStatusCode(HttpStatus.valueOf(error.status()));
            wrappedBody = ApiResult.fail(error, traceId);
        } else {
            // 3. 정상 응답인 경우 HTTP 200 OK와 함께 성공(ok) 포맷으로 래핑
            response.setStatusCode(HttpStatus.OK);
            wrappedBody = ApiResult.ok(body, traceId);

            // 4. String 반환 타입 이슈 우회 (StringHttpMessageConverter가 선택된 경우 직접 JSON 직렬화 수행)
            if (StringHttpMessageConverter.class.isAssignableFrom(converterType)) {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(wrappedBody);
            }
        }

        return wrappedBody;
    }
}
