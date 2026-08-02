package academy.javaengineering.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Logging Examples Tests")
class LoggingExamplesTest {

    @Test
    @DisplayName("JulLoggingExample should configure logger")
    void testJulLogging() {
        assertDoesNotThrow(() -> {
            JulLoggingExample.configureLogger();
        });
    }

    @Test
    @DisplayName("Slf4jLoggingExample should log messages")
    void testSlf4jLogging() {
        assertDoesNotThrow(() -> {
            Slf4jLoggingExample.demonstrateBasicLogging();
        });
    }

    @Test
    @DisplayName("LogbackExample should handle MDC context")
    void testLogbackLogging() {
        assertDoesNotThrow(() -> {
            LogbackExample.demonstrateContextualLogging();
        });
    }
}
