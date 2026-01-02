package com.example.lib.trace.starter.internal.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.lib.trace.core.LogTrace;
import com.example.lib.trace.core.Trace;
import com.example.lib.trace.core.TraceLevel;
import com.example.lib.trace.core.TraceStatus;

@ExtendWith(MockitoExtension.class)
class TraceAspectTest {

    @Mock
    private LogTrace logTrace;

    @InjectMocks
    private TraceAspect traceAspect;

    @Test
    @DisplayName("정상 실행 시 트레이스가 시작되고 종료된다")
    void shouldBeginAndEndTraceOnSuccess() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature signature = mock(MethodSignature.class);
        TraceStatus status = mock(TraceStatus.class);

        given(joinPoint.getSignature()).willReturn(signature);
        Method method = TestClass.class.getMethod("annotatedMethod");
        given(signature.getMethod()).willReturn(method);
        // The method has @Trace annotation
        
        given(signature.toShortString()).willReturn("TestClass.annotatedMethod()");
        given(logTrace.begin(anyString(), any(TraceLevel.class))).willReturn(status);
        
        Object expectedResult = new Object();
        given(joinPoint.proceed()).willReturn(expectedResult);

        // when
        Object result = traceAspect.traceMethod(joinPoint);

        // then
        assertThat(result).isSameAs(expectedResult);
        verify(logTrace).begin("TestClass.annotatedMethod()", TraceLevel.DEBUG);
        verify(logTrace).end(status);
    }

    @Test
    @DisplayName("예외 발생 시 트레이스가 예외 메시지와 함께 종료된다")
    void shouldEndTraceWithExceptionMessageOnFailure() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature signature = mock(MethodSignature.class);
        TraceStatus status = mock(TraceStatus.class);

        given(joinPoint.getSignature()).willReturn(signature);
        Method method = TestClass.class.getMethod("annotatedMethod");
        given(signature.getMethod()).willReturn(method);
        
        given(signature.toShortString()).willReturn("TestClass.annotatedMethod()");
        given(logTrace.begin(anyString(), any(TraceLevel.class))).willReturn(status);
        
        Exception originalException = new RuntimeException("Test Exception");
        given(joinPoint.proceed()).willThrow(originalException);

        // when
        Throwable thrown = catchThrowable(() -> traceAspect.traceMethod(joinPoint));

        // then
        assertThat(thrown).isSameAs(originalException);
        verify(logTrace).begin("TestClass.annotatedMethod()", TraceLevel.DEBUG);
        verify(logTrace).end(status, "[EXCEPTION: Test Exception]");
    }

    @Test
    @DisplayName("클래스 레벨의 @Trace 어노테이션이 적용된다")
    void shouldApplyClassLevelTraceAnnotation() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature signature = mock(MethodSignature.class);
        TraceStatus status = mock(TraceStatus.class);

        given(joinPoint.getSignature()).willReturn(signature);
        Method method = AnnotatedClass.class.getMethod("normalMethod");
        given(signature.getMethod()).willReturn(method);
        
        given(joinPoint.getTarget()).willReturn(new AnnotatedClass());
        
        given(signature.toShortString()).willReturn("AnnotatedClass.normalMethod()");
        given(logTrace.begin(anyString(), any(TraceLevel.class))).willReturn(status);
        
        Object expectedResult = new Object();
        given(joinPoint.proceed()).willReturn(expectedResult);

        // when
        Object result = traceAspect.traceMethod(joinPoint);

        // then
        assertThat(result).isSameAs(expectedResult);
        verify(logTrace).begin("AnnotatedClass.normalMethod()", TraceLevel.INFO);
        verify(logTrace).end(status);
    }

    private static class TestClass {
        @Trace(level = TraceLevel.DEBUG)
        public void annotatedMethod() {
        }
    }

    @Trace(level = TraceLevel.INFO)
    private static class AnnotatedClass {
        public void normalMethod() {
        }
    }
}
