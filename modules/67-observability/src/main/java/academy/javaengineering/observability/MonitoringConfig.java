package academy.javaengineering.observability;

/**
 * Demonstrates monitoring configuration.
 */
public class MonitoringConfig {

    public record MetricConfig(
        String name,
        String type,
        String description
    ) {}

    public static java.util.List<MetricConfig> getJavaMetrics() {
        return java.util.List.of(
            new MetricConfig("jvm.memory.used", "gauge", "JVM memory usage"),
            new MetricConfig("jvm.gc.pause", "timer", "GC pause duration"),
            new MetricConfig("http.requests.total", "counter", "Total HTTP requests"),
            new MetricConfig("http.requests.duration", "timer", "HTTP request duration"),
            new MetricConfig("db.connections.active", "gauge", "Active DB connections")
        );
    }

    public static java.util.Map<String, String> getAlertRules() {
        return java.util.Map.of(
            "HighCPU", "cpu_usage > 80% for 5 minutes",
            "HighMemory", "memory_usage > 90% for 5 minutes",
            "HighErrorRate", "error_rate > 5% for 5 minutes",
            "SlowResponses", "p95_latency > 1000ms"
        );
    }
}
