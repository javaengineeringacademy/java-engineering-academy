package academy.javaengineering.jvm.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 3: GC Parameter Tuning
 *
 * Task: Tune GC parameters for a web server workload simulation.
 * Goal: Keep pause times under 100ms while maintaining high throughput.
 */
public class Exercise3 {

    private static final int REQUESTS = 10000;
    private static final List<byte[]> sessionData = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== GC Tuning ===\n");

        long startTime = System.currentTimeMillis();
        int completed = 0;

        for (int i = 0; i < REQUESTS; i++) {
            processRequest(i);
            completed++;

            if (completed % 1000 == 0) {
                System.out.printf("  Completed %d requests%n", completed);
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.printf("  Total time: %d ms%n", elapsed);
        System.out.printf("  Throughput: %.1f req/s%n", (REQUESTS * 1000.0 / elapsed));

        // TODO: Try different JVM flags and compare results
        // java -XX:+UseSerialGC Exercise3
        // java -XX:+UseParallelGC Exercise3
        // java -XX:+UseG1GC -XX:MaxGCPauseMillis=100 Exercise3
        // java -XX:+UseZGC Exercise3
    }

    static void processRequest(int id) {
        // Simulate web request: allocate session data, process, discard
        byte[] data = new byte[4096]; // 4KB per request
        sessionData.add(data);

        // Simulate processing
        double result = 0;
        for (int i = 0; i < 100; i++) {
            result += Math.sin(i) * Math.cos(i);
        }

        // Keep only last 100 sessions in memory
        if (sessionData.size() > 100) {
            sessionData.remove(0);
        }
    }
}
