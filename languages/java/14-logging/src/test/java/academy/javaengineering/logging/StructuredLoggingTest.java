package academy.javaengineering.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for structured logging patterns and key-value pairs.
 */
class StructuredLoggingTest {

    @Test
    @DisplayName("Should log key-value pairs using atInfo()")
    void shouldLogKeyValuePairs() {
        StructuredLogging demo = new StructuredLogging();
        assertDoesNotThrow(demo::demonstrateKeyValuePairs);
    }

    @Test
    @DisplayName("Should handle correlation IDs in MDC")
    void shouldHandleCorrelationIds() {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        assertEquals(correlationId, MDC.get("correlationId"));

        MDC.clear();
        assertNull(MDC.get("correlationId"));
    }

    @Test
    @DisplayName("Should handle request tracing context")
    void shouldHandleRequestTracing() {
        String traceId = UUID.randomUUID().toString();
        String spanId = UUID.randomUUID().toString().substring(0, 8);

        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);

        assertEquals(traceId, MDC.get("traceId"));
        assertEquals(spanId, MDC.get("spanId"));

        MDC.clear();
        assertNull(MDC.get("traceId"));
        assertNull(MDC.get("spanId"));
    }

    @Test
    @DisplayName("Should support nested span context")
    void shouldSupportNestedSpanContext() {
        String parentSpanId = "parent-123";
        String childSpanId = "child-456";

        MDC.put("spanId", parentSpanId);
        assertEquals(parentSpanId, MDC.get("spanId"));

        MDC.put("parentSpanId", parentSpanId);
        MDC.put("spanId", childSpanId);

        assertEquals(parentSpanId, MDC.get("parentSpanId"));
        assertEquals(childSpanId, MDC.get("spanId"));

        MDC.clear();
    }

    @Test
    @DisplayName("Should handle audit logging with context")
    void shouldHandleAuditLogging() {
        StructuredLogging demo = new StructuredLogging();
        assertDoesNotThrow(demo::demonstrateAuditLogging);
    }

    @Test
    @DisplayName("Should handle error context properly")
    void shouldHandleErrorContext() {
        StructuredLogging demo = new StructuredLogging();
        assertDoesNotThrow(demo::demonstrateErrorContext);
    }

    @Test
    @DisplayName("Should support metric logging patterns")
    void shouldSupportMetricLogging() {
        StructuredLogging demo = new StructuredLogging();
        assertDoesNotThrow(demo::demonstrateMetricsLogging);
    }
}
