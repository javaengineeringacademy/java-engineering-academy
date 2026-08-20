package academy.javaengineering.testing.junit5.advanced.solutions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import java.lang.annotation.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class Exercise3ExtensionSolution {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(RetryExtension.class)
    @interface Retry {
        int value() default 3;
    }

    static class RetryExtension implements TestExecutionExceptionHandler {
        @Override
        public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
            Retry retry = context.getRequiredTestMethod().getAnnotation(Retry.class);
            if (retry == null) throw throwable;

            int maxRetries = retry.value();
            for (int i = 0; i < maxRetries - 1; i++) {
                try {
                    Method method = context.getRequiredTestMethod();
                    Object instance = context.getRequiredTestInstance();
                    method.invoke(instance);
                    return; // Passed on retry
                } catch (Exception e) {
                    if (i == maxRetries - 2) throw throwable;
                }
            }
        }
    }

    int attempt = 0;

    @Retry(3)
    @Test
    void shouldRetryOnFailure() {
        attempt++;
        assertTrue(attempt >= 2, "Fails first time, passes on retry");
    }
}
