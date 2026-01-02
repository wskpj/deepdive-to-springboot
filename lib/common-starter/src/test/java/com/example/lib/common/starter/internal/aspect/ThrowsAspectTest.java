package com.example.lib.common.starter.internal.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.lib.common.core.annotation.Throws;
import com.example.lib.common.core.exception.BaseException;
import com.example.lib.common.core.exception.UnhandledException;

@ExtendWith(MockitoExtension.class)
class ThrowsAspectTest {

    @InjectMocks
    private ThrowsAspect aspect;

    @Test
    @DisplayName("정상 실행 시 예외 없이 결과를 반환한다")
    void shouldReturnResultOnSuccess() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Throws throwsAnnotation = mock(Throws.class);
        Object expectedResult = new Object();
        given(joinPoint.proceed()).willReturn(expectedResult);

        // when
        Object result = aspect.translate(joinPoint, throwsAnnotation);

        // then
        assertThat(result).isSameAs(expectedResult);
    }

    @Test
    @DisplayName("이미 BaseException 계열인 경우 변환 없이 그대로 던진다")
    void shouldThrowBaseExceptionAsIs() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Throws throwsAnnotation = mock(Throws.class);
        BaseException expectedException = new UnhandledException(new RuntimeException("Test"));
        given(joinPoint.proceed()).willThrow(expectedException);

        // when
        Throwable thrown = catchThrowable(() -> aspect.translate(joinPoint, throwsAnnotation));

        // then
        assertThat(thrown).isSameAs(expectedException);
    }

    @Test
    @DisplayName("일반 예외 발생 시 지정된 BaseException으로 변환하여 던진다")
    void shouldTranslateException() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Throws throwsAnnotation = mock(Throws.class);
        doReturn(UnhandledException.class).when(throwsAnnotation).value();
        
        Exception originalException = new RuntimeException("Original Exception");
        given(joinPoint.proceed()).willThrow(originalException);

        // when
        Throwable thrown = catchThrowable(() -> aspect.translate(joinPoint, throwsAnnotation));

        // then
        assertThat(thrown)
                .isInstanceOf(UnhandledException.class)
                .hasCause(originalException);
    }

    @Test
    @DisplayName("지정된 예외로 변환 실패 시(적절한 생성자 없음 등) 원래 예외를 그대로 던진다")
    void shouldThrowOriginalExceptionWhenTranslationFails() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Throws throwsAnnotation = mock(Throws.class);
        
        // BaseException은 추상적인 성격을 띄고 Throwable을 받는 public 생성자가 없기 때문에 리플렉션으로 생성 불가
        doReturn(BaseException.class).when(throwsAnnotation).value();
        
        Exception originalException = new RuntimeException("Original Exception");
        given(joinPoint.proceed()).willThrow(originalException);

        // when
        Throwable thrown = catchThrowable(() -> aspect.translate(joinPoint, throwsAnnotation));

        // then
        assertThat(thrown).isSameAs(originalException);
    }
}
