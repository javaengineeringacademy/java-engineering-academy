package academy.javaengineering.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for logging best practices: parameterized messages, MDC, and patterns.
 */
class LoggingBestPracticesTest {

    @Test
    @DisplayName("Should log parameterized messages without string concatenation")
    void shouldLogParameterizedMessages() {
        LoggingBestPractices demo = new LoggingBestPractices();
        assertDoesNotThrow(demo::demonstrateParameterizedMessages);
    }

    @Test
    @DisplayName("Should handle lazy evaluation correctly")
    void shouldHandleLazyEvaluation() {
        LoggingBestPractices demo = new LoggingBestPractices();
        assertDoesNotThrow(demo::demonstrateLazyEvaluation);
    }

    @Test
    @DisplayName("Should handle MDC context properly")
    void shouldHandleMDCContext() {
        MDC.put("testKey", "testValue");
        assertEquals("testValue", MDC.get("testKey"));

        MDC.clear();
        assertNull(MDC.get("testKey"));
    }

    @Test
    @DisplayName("Should handle multiple MDC entries")
    void shouldHandleMultipleMDCEntries() {
        MDC.put("userId", "USR-42");
        MDC.put("sessionId", "SES-123");
        MDC.put("requestId", "REQ-456");

        assertEquals("USR-42", MDC.get("userId"));
        assertEquals("SES-123", MDC.get("sessionId"));
        assertEquals("REQ-456", MDC.get("requestId"));

        MDC.clear();
        assertNull(MDC.get("userId"));
    }

    @Test
    @DisplayName("Should handle structured log patterns")
    void shouldHandleStructuredLogPatterns() {
        LoggingBestPractices demo = new LoggingBestPractices();
        assertDoesNotThrow(demo::demonstrateStructuredLogging);
    }

    @Test
    @DisplayName("Should preserve context across operations")
    void shouldPreserveContextAcrossOperations() {
        MDC.put("traceId", "trace-test-123");
        MDC.put("spanId", "span-test-456");

        assertEquals("trace-test-123", MDC.get("traceId"));
        assertEquals("span-test-456", MDC.get("spanId"));

        MDC.remove("traceId");
        assertNull(MDC.get("traceId"));
        assertEquals("span-test-456", MDC.get("spanId"));

        MDC.clear();
    }

    @Test
    @DisplayName("Should handle exception logging with context")
    void shouldHandleExceptionLogging() {
        LoggingBestPractices demo = new LoggingBestPractices();
        assertDoesNotThrow(demo::demonstrateExceptionHandling);
    }

    @Test
    @DisplayName("Should handle performance considerations")
    void shouldHandlePerformanceConsiderations() {
        LoggingBestPractices demo = new LoggingBestPractices();
        assertDoesNotThrow(demo::demonstratePerformanceConsiderations);
    }

    @Test
    @DisplayName("Should support MDC remove operation")
    void shouldSupportMDCRemoveOperation() {
        MDC.put("key1", "value1");
        MDC.put("key2", "value2");

        MDC.remove("key1");
        assertNull(MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));

        MDC.clear();
    }

    @Test
    @DisplayName("Should handle MDC with thread isolation")
    void shouldHandleMDCWithThreadIsolation() throws InterruptedException {
        MDC.put("mainThread", "mainValue");

        Thread otherThread = new Thread(() -> {
            assertNull(MDC.get("mainThread"));
            MDC.put("otherThread", "otherValue");
            assertEquals("otherValue", MDC.get("otherThread"));
            MDC.clear();
        });

        otherThread.start();
        otherThread.join();

        assertEquals("mainValue", MDC.get("mainThread"));
        MDC.clear();
    }

    @Test
    @DisplayName("Should handle security logging patterns")
    void shouldHandleSecurityLoggingPatterns() {
        LoggingBestPractices demo = new LoggingBestPractices();
        assertDoesNotThrow(demo::demonstrateSecurityLogging);
    }

    @Test
    @DisplayName("Should handle structured message format")
    void shouldHandleStructuredMessageFormat() {
        Logger logger = LoggerFactory.getLogger(LoggingBestPracticesTest.class);
        assertNotNull(logger);
        assertTrue(logger.isInfoEnabled());
    }
}
