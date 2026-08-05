package academy.javaengineering.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SLF4J basics: logger creation, log levels, and core functionality.
 */
class Slf4jBasicsTest {

    @Test
    @DisplayName("Should create logger from class reference")
    void shouldCreateLoggerFromClassReference() {
        Logger logger = LoggerFactory.getLogger(Slf4jBasicsTest.class);
        assertNotNull(logger);
        assertEquals("academy.javaengineering.logging.Slf4jBasicsTest", logger.getName());
    }

    @Test
    @DisplayName("Should create logger with custom name")
    void shouldCreateLoggerWithCustomName() {
        Logger logger = LoggerFactory.getLogger("CustomTestLogger");
        assertNotNull(logger);
        assertEquals("CustomTestLogger", logger.getName());
    }

    @Test
    @DisplayName("Should return same logger instance for same class")
    void shouldReturnSameLoggerInstance() {
        Logger logger1 = LoggerFactory.getLogger(Slf4jBasics.class);
        Logger logger2 = LoggerFactory.getLogger(Slf4jBasics.class);
        assertSame(logger1, logger2);
    }

    @Test
    @DisplayName("Should check log level enabled status")
    void shouldCheckLogLevelEnabled() {
        Logger logger = LoggerFactory.getLogger(Slf4jBasicsTest.class);
        assertNotNull(logger);
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        boolean isTraceEnabled = logger.isTraceEnabled();
        boolean isDebugEnabled = logger.isDebugEnabled();
        assertNotNull(Boolean.valueOf(isTraceEnabled));
        assertNotNull(Boolean.valueOf(isDebugEnabled));
    }

    @Test
    @DisplayName("Should not throw when logging at any level")
    void shouldNotThrowWhenLoggingAtAnyLevel() {
        Slf4jBasics demo = new Slf4jBasics();
        assertDoesNotThrow(demo::demonstrateLogLevels);
        assertDoesNotThrow(demo::demonstrateParameterizedLogging);
        assertDoesNotThrow(demo::demonstrateExceptionLogging);
    }

    @Test
    @DisplayName("Should handle multiple parameters correctly")
    void shouldHandleMultipleParameters() {
        Slf4jBasics demo = new Slf4jBasics();
        assertDoesNotThrow(demo::demonstrateMultipleParameters);
    }

    @Test
    @DisplayName("Should handle markers without errors")
    void shouldHandleMarkers() {
        Slf4jBasics demo = new Slf4jBasics();
        assertDoesNotThrow(demo::demonstrateMarkers);
    }

    @Test
    @DisplayName("Should create valid marker instances")
    void shouldCreateValidMarkerInstances() {
        Marker marker = MarkerFactory.getMarker("TEST_MARKER");
        assertNotNull(marker);
        assertEquals("TEST_MARKER", marker.getName());
    }

    @Test
    @DisplayName("Should support marker hierarchy")
    void shouldSupportMarkerHierarchy() {
        Marker parent = MarkerFactory.getMarker("PARENT");
        Marker child = MarkerFactory.getMarker("CHILD");
        child.add(parent);
        assertTrue(child.contains(parent));
    }

    @Test
    @DisplayName("Should handle fluent API logging")
    void shouldHandleFluentApiLogging() {
        Slf4jBasics demo = new Slf4jBasics();
        assertDoesNotThrow(demo::demonstrateFluentApi);
    }

    @Test
    @DisplayName("Should handle nested exception logging")
    void shouldHandleNestedExceptionLogging() {
        Slf4jBasics demo = new Slf4jBasics();
        assertDoesNotThrow(demo::demonstrateNestedExceptionLogging);
    }

    @Test
    @DisplayName("Should get logger with different names")
    void shouldGetLoggerWithDifferentNames() {
        Logger logger1 = LoggerFactory.getLogger("com.example.Logger1");
        Logger logger2 = LoggerFactory.getLogger("com.example.Logger2");
        assertNotEquals(logger1.getName(), logger2.getName());
    }
}
