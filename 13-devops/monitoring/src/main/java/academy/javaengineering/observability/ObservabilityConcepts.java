package academy.javaengineering.observability;

/**
 * Demonstrates observability concepts.
 */
public class ObservabilityConcepts {

    public record TelemetryData(
        String type,
        String name,
        double value,
        java.util.Map<String, String> tags
    ) {}

    public static java.util.List<String> getThreePillars() {
        return java.util.List.of(
            "Logging - Structured event records",
            "Metrics - Numerical measurements over time",
            "Tracing - Request flow across services"
        );
    }

    public static java.util.Map<String, String> getTools() {
        return java.util.Map.of(
            "Logging", "ELK Stack, Fluentd, Logback",
            "Metrics", "Prometheus, Micrometer, Grafana",
            "Tracing", "Jaeger, Zipkin, OpenTelemetry"
        );
    }
}
