package academy.javaengineering.performance;

/**
 * Demonstrates performance measurement utilities.
 */
public class PerformanceProfiler {

    public record ProfileResult(
        String operationName,
        long durationMs,
        long memoryUsedBytes,
        double operationsPerSecond
    ) {}

    public ProfileResult measure(String operationName, Runnable operation) {
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        
        operation.run();
        
        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        long durationMs = (endTime - startTime) / 1_000_000;
        long memoryUsed = endMemory - startMemory;
        double opsPerSecond = durationMs > 0 ? 1000.0 / durationMs : 0;
        
        return new ProfileResult(operationName, durationMs, memoryUsed, opsPerSecond);
    }

    public void printProfile(ProfileResult result) {
        System.out.printf("Operation: %s%n", result.operationName());
        System.out.printf("Duration: %d ms%n", result.durationMs());
        System.out.printf("Memory: %d bytes%n", result.memoryUsedBytes());
        System.out.printf("Ops/sec: %.2f%n", result.operationsPerSecond());
    }
}
