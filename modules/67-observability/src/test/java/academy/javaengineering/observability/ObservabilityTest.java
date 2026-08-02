package academy.javaengineering.observability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Observability Tests")
class ObservabilityTest {

    @Test
    @DisplayName("Should have three pillars of observability")
    void testThreePillars() {
        var pillars = ObservabilityConcepts.getThreePillars();
        assertEquals(3, pillars.size());
        assertTrue(pillars.stream().anyMatch(p -> p.contains("Logging")));
        assertTrue(pillars.stream().anyMatch(p -> p.contains("Metrics")));
        assertTrue(pillars.stream().anyMatch(p -> p.contains("Tracing")));
    }

    @Test
    @DisplayName("Should have Java metrics")
    void testJavaMetrics() {
        var metrics = MonitoringConfig.getJavaMetrics();
        assertFalse(metrics.isEmpty());
        assertTrue(metrics.stream().anyMatch(m -> m.name().contains("jvm")));
    }

    @Test
    @DisplayName("Should have alert rules")
    void testAlertRules() {
        var rules = MonitoringConfig.getAlertRules();
        assertFalse(rules.isEmpty());
        assertTrue(rules.containsKey("HighCPU"));
    }
}
