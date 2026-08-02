package academy.javaengineering.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class demonstrating logging testing strategies.
 */
class LoggingTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);
    private String originalRequestId;

    @BeforeEach
    void setUp() {
        originalRequestId = MDC.get("requestId");
        MDC.put("requestId", UUID.randomUUID().toString());
    }

    @AfterEach
    void tearDown() {
        if (originalRequestId != null) {
            MDC.put("requestId", originalRequestId);
        } else {
            MDC.clear();
        }
    }

    @Test
    void testBasicLogging() {
        logger.info("Running testBasicLogging");
        
        // Test that logging doesn't throw exceptions
        assertDoesNotThrow(() -> {
            logger.trace("Trace message");
            logger.debug("Debug message");
            logger.info("Info message");
            logger.warn("Warn message");
            logger.error("Error message");
        });
    }

    @Test
    void testParameterizedLogging() {
        logger.info("Running testParameterizedLogging");
        
        String userId = "USER-TEST-1";
        int count = 5;
        
        // Test parameterized logging
        assertDoesNotThrow(() -> {
            logger.info("User {} has {} items", userId, count);
            logger.debug("Processing user: {} with {} items", userId, count);
        });
    }

    @Test
    void testMdcLogging() {
        logger.info("Running testMdcLogging");
        
        MDC.put("userId", "USER-MDC-1");
        MDC.put("operation", "TEST");
        
        try {
            logger.info("Testing MDC logging");
            
            // Verify MDC values are set
            assertEquals("USER-MDC-1", MDC.get("userId"));
            assertEquals("TEST", MDC.get("operation"));
            
        } finally {
            MDC.remove("userId");
            MDC.remove("operation");
        }
    }

    @Test
    void testMarkerLogging() {
        logger.info("Running testMarkerLogging");
        
        Marker auditMarker = MarkerFactory.getMarker("AUDIT");
        Marker performanceMarker = MarkerFactory.getMarker("PERFORMANCE");
        
        // Test marker logging
        assertDoesNotThrow(() -> {
            logger.info(auditMarker, "Audit marker test");
            logger.info(performanceMarker, "Performance marker test");
        });
    }

    @Test
    void testExceptionLogging() {
        logger.info("Running testExceptionLogging");
        
        Exception testException = new RuntimeException("Test exception");
        
        // Test exception logging
        assertDoesNotThrow(() -> {
            logger.error("Error with exception: {}", testException.getMessage(), testException);
        });
    }

    @Test
    void testMdcCleanup() {
        logger.info("Running testMdcCleanup");
        
        String testKey = "testKey";
        String testValue = "testValue";
        
        MDC.put(testKey, testValue);
        assertEquals(testValue, MDC.get(testKey));
        
        MDC.remove(testKey);
        assertNull(MDC.get(testKey));
    }

    @Test
    void testMdcClear() {
        logger.info("Running testMdcClear");
        
        MDC.put("key1", "value1");
        MDC.put("key2", "value2");
        
        assertNotNull(MDC.get("key1"));
        assertNotNull(MDC.get("key2"));
        
        MDC.clear();
        
        assertNull(MDC.get("key1"));
        assertNull(MDC.get("key2"));
    }

    @Test
    void testSensitiveDataMasking() {
        logger.info("Running testSensitiveDataMasking");
        
        String sensitiveData = "4111-1111-1111-1111";
        String maskedData = maskSensitiveData(sensitiveData);
        
        assertNotEquals(sensitiveData, maskedData);
        assertTrue(maskedData.endsWith("1111"));
        assertTrue(maskedData.startsWith("****"));
    }

    @Test
    void testLoggingPerformance() {
        logger.info("Running testLoggingPerformance");
        
        long startTime = System.currentTimeMillis();
        int iterations = 10000;
        
        for (int i = 0; i < iterations; i++) {
            logger.debug("Iteration: {}", i);
        }
        
        long duration = System.currentTimeMillis() - startTime;
        
        // Logging should be reasonably fast
        assertTrue(duration < 1000, "Logging took too long: " + duration + "ms");
        
        logger.info("Logged {} iterations in {}ms", iterations, duration);
    }

    @Test
    void testLogLevelCheck() {
        logger.info("Running testLogLevelCheck");
        
        // Test level checking
        boolean isDebugEnabled = logger.isDebugEnabled();
        boolean isTraceEnabled = logger.isTraceEnabled();
        
        // At least INFO should be enabled
        assertTrue(logger.isInfoEnabled());
        
        // Debug and trace may or may not be enabled
        // Just ensure the methods work
        assertDoesNotThrow(() -> {
            if (isDebugEnabled) {
                logger.debug("Debug is enabled");
            }
            if (isTraceEnabled) {
                logger.trace("Trace is enabled");
            }
        });
    }

    @Test
    void testLoggingWithReflection() throws Exception {
        logger.info("Running testLoggingWithReflection");
        
        // Test that Logger class has expected methods
        Method[] methods = Logger.class.getMethods();
        
        boolean hasInfoMethod = false;
        boolean hasDebugMethod = false;
        boolean hasErrorMethod = false;
        
        for (Method method : methods) {
            String methodName = method.getName();
            if ("info".equals(methodName)) hasInfoMethod = true;
            if ("debug".equals(methodName)) hasDebugMethod = true;
            if ("error".equals(methodName)) hasErrorMethod = true;
        }
        
        assertTrue(hasInfoMethod, "Logger should have info method");
        assertTrue(hasDebugMethod, "Logger should have debug method");
        assertTrue(hasErrorMethod, "Logger should have error method");
    }

    @Test
    void testLoggerFactory() {
        logger.info("Running testLoggerFactory");
        
        // Test LoggerFactory
        Logger testLogger = LoggerFactory.getLogger(LoggingTest.class);
        assertNotNull(testLogger);
        
        Logger namedLogger = LoggerFactory.getLogger("TestLogger");
        assertNotNull(namedLogger);
    }

    // Helper method for sensitive data masking
    private String maskSensitiveData(String data) {
        if (data == null || data.length() < 4) {
            return "****";
        }
        return "*".repeat(data.length() - 4) + data.substring(data.length() - 4);
    }
}