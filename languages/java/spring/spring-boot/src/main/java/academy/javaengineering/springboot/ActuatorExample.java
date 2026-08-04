package academy.javaengineering.springboot;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates Spring Boot monitoring concepts using standard Spring beans.
 *
 * <p>This class showcases:
 * <ul>
 *   <li>Custom health check implementations for application monitoring</li>
 *   <li>Application metadata and info collection</li>
 *   <li>Custom metrics tracking and reporting</li>
 *   <li>Health status tracking over time</li>
 * </ul>
 */
@Component
public class ActuatorExample {

    private final HealthTracker healthTracker = new HealthTracker();
    private final MetricsCollector metricsCollector = new MetricsCollector();

    /**
     * Custom health check for database connectivity.
     */
    @Component
    public static class DatabaseHealthCheck {

        private final Random random = new Random();

        public Map<String, Object> checkHealth() {
            boolean isUp = random.nextInt(100) > 5;

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", isUp ? "UP" : "DOWN");
            result.put("database", "PostgreSQL");
            result.put("connectionPool", "active");
            result.put("activeConnections", 10);
            result.put("maxConnections", 20);

            if (!isUp) {
                result.put("error", "Connection refused");
            }

            return result;
        }
    }

    /**
     * Custom health check for external service connectivity.
     */
    @Component
    public static class ExternalServiceHealthCheck {

        public Map<String, Object> checkHealth() {
            boolean isAvailable = true;

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", isAvailable ? "UP" : "DOWN");
            result.put("service", "PaymentGateway");
            result.put("latency", "45ms");
            result.put("status", isAvailable ? "operational" : "unavailable");

            return result;
        }
    }

    /**
     * Custom health check for disk space.
     */
    @Component
    public static class DiskSpaceHealthCheck {

        public Map<String, Object> checkHealth() {
            long freeSpaceMB = 5120;
            long thresholdMB = 1024;
            boolean isHealthy = freeSpaceMB > thresholdMB;

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", isHealthy ? "UP" : "DOWN");
            result.put("disk", isHealthy ? "healthy" : "low disk space");
            result.put("freeSpaceMB", freeSpaceMB);
            result.put("thresholdMB", thresholdMB);

            return result;
        }
    }

    /**
     * Application info collector that gathers application metadata.
     */
    @Component
    public static class ApplicationInfoCollector {

        public Map<String, Object> collectInfo() {
            Map<String, Object> appInfo = new LinkedHashMap<>();
            appInfo.put("name", "Spring Boot Demo");
            appInfo.put("version", "1.0.0");
            appInfo.put("description", "Demonstrates Spring Boot monitoring features");
            appInfo.put("author", "Java Engineering Academy");

            Map<String, Object> buildInfo = new HashMap<>();
            buildInfo.put("javaVersion", System.getProperty("java.version"));
            buildInfo.put("osName", System.getProperty("os.name"));
            buildInfo.put("osArch", System.getProperty("os.arch"));
            buildInfo.put("timestamp", java.time.Instant.now().toString());

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("application", appInfo);
            result.put("build", buildInfo);

            return result;
        }
    }

    /**
     * Feature info collector that gathers feature status.
     */
    @Component
    public static class FeatureInfoCollector {

        public Map<String, Object> collectInfo() {
            Map<String, Object> features = new LinkedHashMap<>();
            features.put("logging", Map.of("enabled", true, "level", "INFO"));
            features.put("metrics", Map.of("enabled", true, "interval", "30s"));
            features.put("cache", Map.of("enabled", false, "provider", "none"));
            features.put("security", Map.of("enabled", true, "method", "JWT"));

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("features", features);

            return result;
        }
    }

    /**
     * Health tracker that monitors application health status over time.
     */
    public static class HealthTracker {

        private final Map<String, String> healthStatuses = new LinkedHashMap<>();
        private final AtomicInteger checkCount = new AtomicInteger(0);

        public void recordHealthCheck(String component, String status) {
            healthStatuses.put(component, status);
            checkCount.incrementAndGet();
        }

        public String getStatus(String component) {
            return healthStatuses.get(component);
        }

        public Map<String, String> getAllStatuses() {
            return new LinkedHashMap<>(healthStatuses);
        }

        public int getCheckCount() {
            return checkCount.get();
        }

        public boolean isHealthy() {
            return healthStatuses.values().stream()
                    .allMatch("UP"::equals);
        }

        public void reset() {
            healthStatuses.clear();
            checkCount.set(0);
        }
    }

    /**
     * Metrics collector for tracking application metrics.
     */
    public static class MetricsCollector {

        private final Map<String, Long> counters = new LinkedHashMap<>();
        private final Map<String, Double> gauges = new LinkedHashMap<>();

        public void incrementCounter(String name) {
            counters.merge(name, 1L, Long::sum);
        }

        public void incrementCounter(String name, long amount) {
            counters.merge(name, amount, Long::sum);
        }

        public Long getCounter(String name) {
            return counters.getOrDefault(name, 0L);
        }

        public void setGauge(String name, double value) {
            gauges.put(name, value);
        }

        public Double getGauge(String name) {
            return gauges.get(name);
        }

        public Map<String, Long> getAllCounters() {
            return new LinkedHashMap<>(counters);
        }

        public Map<String, Double> getAllGauges() {
            return new LinkedHashMap<>(gauges);
        }

        public void reset() {
            counters.clear();
            gauges.clear();
        }
    }

    /**
     * Main method demonstrating monitoring concepts.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Spring Boot Monitoring Examples ===");
        System.out.println();

        // Demonstrate Health Checks
        System.out.println("--- Health Checks ---");
        DatabaseHealthCheck dbHealth = new DatabaseHealthCheck();
        Map<String, Object> dbHealthResult = dbHealth.checkHealth();
        System.out.println("Database Health: " + dbHealthResult.get("status"));
        System.out.println("Database Details: " + dbHealthResult);

        ExternalServiceHealthCheck extHealth = new ExternalServiceHealthCheck();
        Map<String, Object> extHealthResult = extHealth.checkHealth();
        System.out.println("External Service Health: " + extHealthResult.get("status"));

        DiskSpaceHealthCheck diskHealth = new DiskSpaceHealthCheck();
        Map<String, Object> diskHealthResult = diskHealth.checkHealth();
        System.out.println("Disk Space Health: " + diskHealthResult.get("status"));
        System.out.println();

        // Demonstrate Health Tracker
        System.out.println("--- Health Tracker ---");
        HealthTracker tracker = new HealthTracker();
        tracker.recordHealthCheck("database", "UP");
        tracker.recordHealthCheck("external-service", "UP");
        tracker.recordHealthCheck("disk", "UP");
        System.out.println("All Healthy: " + tracker.isHealthy());
        System.out.println("Check Count: " + tracker.getCheckCount());
        System.out.println("All Statuses: " + tracker.getAllStatuses());
        System.out.println();

        // Demonstrate Metrics Collector
        System.out.println("--- Metrics Collector ---");
        MetricsCollector metrics = new MetricsCollector();
        metrics.incrementCounter("http.requests");
        metrics.incrementCounter("http.requests");
        metrics.incrementCounter("http.requests", 5);
        metrics.setGauge("jvm.memory.used", 256.0);
        metrics.setGauge("jvm.memory.max", 512.0);
        System.out.println("Request Count: " + metrics.getCounter("http.requests"));
        System.out.println("Memory Used: " + metrics.getGauge("jvm.memory.used") + "MB");
        System.out.println("All Counters: " + metrics.getAllCounters());
        System.out.println("All Gauges: " + metrics.getAllGauges());
        System.out.println();

        // Demonstrate Info Collectors
        System.out.println("--- Info Collectors ---");
        ApplicationInfoCollector appInfoCollector = new ApplicationInfoCollector();
        Map<String, Object> appInfo = appInfoCollector.collectInfo();
        System.out.println("Application Info: " + appInfo);

        FeatureInfoCollector featureInfoCollector = new FeatureInfoCollector();
        Map<String, Object> featureInfo = featureInfoCollector.collectInfo();
        System.out.println("Feature Info: " + featureInfo);
        System.out.println();

        System.out.println("--- Monitoring Configuration ---");
        System.out.println("health.check.interval=30s");
        System.out.println("metrics.export.enabled=true");
        System.out.println("info.collection.enabled=true");
    }
}
