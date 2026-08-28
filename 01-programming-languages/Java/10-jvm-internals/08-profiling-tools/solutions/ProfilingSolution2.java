package academy.javaengineering.jvm.profiling;

import jdk.jfr.Configuration;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Description;
import jdk.jfr.Category;
import jdk.jfr.Recording;
import java.time.Duration;

/**
 * Solution 2: JFR Recording Exercise - Custom events and programmatic recording
 */
public class ProfilingSolution2 {

    @Label("API Request")
    @Description("Tracks incoming API requests")
    @Category("Application.Web")
    static class ApiRequestEvent extends Event {
        @Label("Endpoint")
        String endpoint;

        @Label("HTTP Method")
        String method;

        @Label("Status Code")
        int statusCode;

        @Label("Duration (ms)")
        long durationMs;
    }

    @Label("Cache Operation")
    @Description("Tracks cache hit/miss patterns")
    @Category("Application.Cache")
    static class CacheOperationEvent extends Event {
        @Label("Cache Name")
        String cacheName;

        @Label("Key")
        String key;

        @Label("Hit")
        boolean hit;

        @Label("Duration (ns)")
        long durationNanos;
    }

    @Label("File I/O")
    @Description("Tracks file system operations")
    @Category("Application.IO")
    static class FileIoEvent extends Event {
        @Label("File Path")
        String filePath;

        @Label("Operation")
        String operation;

        @Label("Bytes Transferred")
        long bytesTransferred;

        @Label("Duration (ms)")
        long durationMs;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== JFR Recording Solution ===\n");

        printAvailableConfigurations();

        // Create recording with default configuration
        try (Recording recording = new Recording()) {
            System.out.println("Recording with default configuration:");
            recording.enable("jdk.GarbageCollection");
            recording.enable("jdk.CPULoad");
            recording.enable(ApiRequestEvent.class);
            recording.enable(CacheOperationEvent.class);
            recording.enable(FileIoEvent.class);

            recording.setDuration(Duration.ofSeconds(3));
            recording.start();

            System.out.println("Recording started, emitting custom events...\n");

            // Simulate API requests
            for (int i = 0; i < 5; i++) {
                emitApiRequest("/api/users/" + i, "GET", 200, (long) (Math.random() * 100) + 10);
            }

            // Simulate cache operations
            for (int i = 0; i < 8; i++) {
                boolean hit = Math.random() > 0.3;
                emitCacheOperation("userCache", "user:" + i, hit, (long) (Math.random() * 10000));
            }

            // Simulate file I/O
            for (int i = 0; i < 3; i++) {
                emitFileIo("/data/export_" + i + ".csv", "write", (long) (Math.random() * 1000000),
                        (long) (Math.random() * 50) + 5);
            }

            recording.stop();

            System.out.println("\nRecording stopped.");
            System.out.println("Events recorded. In production, you would analyze the .jfr file");
            System.out.println("with JDK Mission Control (JMC) for detailed analysis.");
        }

        printRecordingOptions();
    }

    static void emitApiRequest(String endpoint, String method, int statusCode, long durationMs) {
        ApiRequestEvent event = new ApiRequestEvent();
        event.endpoint = endpoint;
        event.method = method;
        event.statusCode = statusCode;
        event.durationMs = durationMs;
        event.begin();
        try {
            Thread.sleep(durationMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        event.end();
        event.commit();
        System.out.printf("  API: %s %s -> %d (%d ms)%n", method, endpoint, statusCode, durationMs);
    }

    static void emitCacheOperation(String cacheName, String key, boolean hit, long durationNanos) {
        CacheOperationEvent event = new CacheOperationEvent();
        event.cacheName = cacheName;
        event.key = key;
        event.hit = hit;
        event.durationNanos = durationNanos;
        event.begin();
        try {
            Thread.sleep(durationNanos / 1_000_000); // Convert ns to ms for simulation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        event.end();
        event.commit();
        System.out.printf("  Cache: %s lookup=%s -> %s (%d ns)%n",
                cacheName, key, hit ? "HIT" : "MISS", durationNanos);
    }

    static void emitFileIo(String filePath, String operation, long bytes, long durationMs) {
        FileIoEvent event = new FileIoEvent();
        event.filePath = filePath;
        event.operation = operation;
        event.bytesTransferred = bytes;
        event.durationMs = durationMs;
        event.begin();
        try {
            Thread.sleep(durationMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        event.end();
        event.commit();
        System.out.printf("  File: %s %s %d bytes (%d ms)%n", operation, filePath, bytes, durationMs);
    }

    static void printAvailableConfigurations() throws Exception {
        System.out.println("Available JFR configurations:");
        for (String configName : new String[]{"default", "profile", "minimal"}) {
            Configuration config = Configuration.getConfiguration(configName);
            System.out.printf("  %-12s - %s%n", configName,
                    config.getDescription() != null ? config.getDescription() : "No description");
        }
        System.out.println();
    }

    static void printRecordingOptions() {
        System.out.println("\nRecording options:");
        System.out.println("  setDuration(Duration)       - Auto-stop after duration");
        System.out.println("  setMaxAge(Duration)         - Keep only recent events");
        System.out.println("  setMaxSize(long)            - Limit recording file size");
        System.out.println("  setDestination(Path)        - Output file path");
        System.out.println("  setDumpOnExit(boolean)      - Dump on JVM shutdown");
        System.out.println("  setCircularMemory(int)      - In-memory circular buffer (KB)");
    }
}
