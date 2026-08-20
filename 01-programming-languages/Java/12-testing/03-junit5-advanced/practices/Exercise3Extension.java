package academy.javaengineering.testing.junit5.advanced.practices;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import java.lang.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 3: Custom Extension
 *
 * Tasks:
 * 1. Create a RetryExtension that retries failed tests
 * 2. Create a custom annotation @Retry
 * 3. Test the extension with a failing test
 */
class Exercise3Extension {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(RetryExtension.class)
    @interface Retry {
        int value() default 3;
    }

    static class RetryExtension implements TestExecutionExceptionHandler {
        @Override
        public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
            // TODO: Implement retry logic
            throw throwable;
        }
    }

    @Retry(3)
    @Test
    void shouldRetryOnFailure() {
        // This test fails sometimes
        assertTrue(Math.random() > 0.5);
    }
}
