package academy.javaengineering.senior.production;

import java.util.*;
import java.util.concurrent.*;

/**
 * Health Check Demo
 * Liveness vs Readiness probes, dependency health checks
 */
public class HealthCheckDemo {

    enum Status { UP, DOWN, DEGRADED }

    static class HealthCheckResult {
        final String name;
        final Status status;
        final String message;
        final long responseTimeMs;

        HealthCheckResult(String name, Status status, String message, long responseTimeMs) {
            this.name = name;
            this.status = status;
            this.message = message;
            this.responseTimeMs = responseTimeMs;
        }
    }

    interface HealthChecker {
        HealthCheckResult check();
    }

    // --- Liveness Probe ---
    static class LivenessProbe {
        private volatile boolean alive = true;

        public HealthCheckResult check() {
            long start = System.currentTimeMillis();
            Status status = alive ? Status.UP : Status.DOWN;
            long elapsed = System.currentTimeMillis() - start;
            return new HealthCheckResult("liveness", status, "Application is " + (alive ? "running" : "dead"), elapsed);
        }

        public void markUnhealthy() { alive = false; }
    }

    // --- Readiness Probe ---
    static class ReadinessProbe {
        private final Map<String, HealthChecker> dependencies = new LinkedHashMap<>();

        public void addDependency(String name, HealthChecker checker) {
            dependencies.put(name, checker);
        }

        public HealthCheckResult check() {
            long start = System.currentTimeMillis();
            boolean allUp = true;
            StringBuilder details = new StringBuilder();

            for (Map.Entry<String, HealthChecker> entry : dependencies.entrySet()) {
                HealthCheckResult result = entry.getValue().check();
                if (result.status != Status.UP) allUp = false;
                details.append(String.format("%s:%s ", entry.getKey(), result.status));
            }

            long elapsed = System.currentTimeMillis() - start;
            Status status = allUp ? Status.UP : Status.DOWN;
            return new HealthCheckResult("readiness", status, details.toString().trim(), elapsed);
        }
    }

    // --- Dependency Health Checks ---
    static class DatabaseChecker implements HealthChecker {
        private final boolean connected;
        public DatabaseChecker(boolean connected) { this.connected = connected; }

        public HealthCheckResult check() {
            long start = System.currentTimeMillis();
            Status status = connected ? Status.UP : Status.DOWN;
            String msg = connected ? "Connected (pool: 8/10 active)" : "Connection refused";
            return new HealthCheckResult("database", status, msg, System.currentTimeMillis() - start);
        }
    }

    static class CacheChecker implements HealthChecker {
        private final boolean healthy;
        public CacheChecker(boolean healthy) { this.healthy = healthy; }

        public HealthCheckResult check() {
            long start = System.currentTimeMillis();
            Status status = healthy ? Status.UP : Status.DEGRADED;
            String msg = healthy ? "Redis connected (latency: 2ms)" : "Redis timeout, using fallback";
            return new HealthCheckResult("cache", status, msg, System.currentTimeMillis() - start);
        }
    }

    static class DiskChecker implements HealthChecker {
        private final double usagePercent;
        public DiskChecker(double usagePercent) { this.usagePercent = usagePercent; }

        public HealthCheckResult check() {
            long start = System.currentTimeMillis();
            Status status = usagePercent < 90 ? Status.UP : Status.DEGRADED;
            String msg = String.format("Disk usage: %.1f%%", usagePercent);
            return new HealthCheckResult("disk", status, msg, System.currentTimeMillis() - start);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Health Check Demo ===\n");

        // Liveness check
        LivenessProbe liveness = new LivenessProbe();
        System.out.println("--- Liveness Probe ---");
        HealthCheckResult result = liveness.check();
        System.out.printf("  %s: %s (%s) [%dms]%n", result.name, result.status, result.message, result.responseTimeMs);

        liveness.markUnhealthy();
        result = liveness.check();
        System.out.printf("  %s: %s (%s) [%dms]%n", result.name, result.status, result.message, result.responseTimeMs);

        // Readiness check with dependencies
        System.out.println("\n--- Readiness Probe ---");
        ReadinessProbe readiness = new ReadinessProbe();
        readiness.addDependency("database", new DatabaseChecker(true));
        readiness.addDependency("cache", new CacheChecker(true));
        readiness.addDependency("disk", new DiskChecker(45.0));

        result = readiness.check();
        System.out.printf("  %s: %s (%s) [%dms]%n", result.name, result.status, result.message, result.responseTimeMs);

        // Simulate degraded state
        System.out.println("\n--- Degraded State ---");
        ReadinessProbe degradedReadiness = new ReadinessProbe();
        degradedReadiness.addDependency("database", new DatabaseChecker(true));
        degradedReadiness.addDependency("cache", new CacheChecker(false));
        degradedReadiness.addDependency("disk", new DiskChecker(95.0));

        result = degradedReadiness.check();
        System.out.printf("  %s: %s (%s) [%dms]%n", result.name, result.status, result.message, result.responseTimeMs);

        // JSON output format
        System.out.println("\n--- Health Check JSON Output ---");
        System.out.println("{");
        System.out.println("  \"status\": \"" + result.status + "\",");
        System.out.println("  \"checks\": {");
        System.out.println("    \"database\": \"UP\",");
        System.out.println("    \"cache\": \"DEGRADED\",");
        System.out.println("    \"disk\": \"DEGRADED\"");
        System.out.println("  }");
        System.out.println("}");
    }
}
