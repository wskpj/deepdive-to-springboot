package com.example.lib.common.starter.internal.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.UnhandledException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionWrapperAspectTest {

    @InjectMocks
    private GlobalExceptionWrappingAspect aspect;

    @Test
    @DisplayName("BaseException은 래핑되지 않고 그대로 던져진다")
    void shouldThrowBaseExceptionAsIs() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        BaseException expectedException = new UnhandledException(new RuntimeException("Test"));
        given(joinPoint.proceed()).willThrow(expectedException);

        // when
        Throwable thrown = catchThrowable(() -> aspect.wrap(joinPoint));

        // then
        assertThat(thrown).isSameAs(expectedException);
    }

    @Test
    @DisplayName("일반 Exception은 UnhandledException으로 래핑된다")
    void shouldWrapStandardExceptionIntoUnhandledException() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Exception originalException = new RuntimeException("Standard Exception");
        given(joinPoint.proceed()).willThrow(originalException);

        // when
        Throwable thrown = catchThrowable(() -> aspect.wrap(joinPoint));

        // then
        assertThat(thrown)
                .isInstanceOf(UnhandledException.class)
                .hasCause(originalException);
    }

    @Test
    @DisplayName("정상 실행 시 결과를 반환한다")
    void shouldReturnResultOnSuccess() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Object expectedResult = new Object();
        given(joinPoint.proceed()).willReturn(expectedResult);

        // when
        Object result = aspect.wrap(joinPoint);

        // then
        assertThat(result).isSameAs(expectedResult);
    }
}
