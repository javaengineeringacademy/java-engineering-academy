package academy.javaengineering.jvm.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution 3: GC Parameter Tuning
 */
public class Solution3 {

    private static final int REQUESTS = 10000;
    private static final List<byte[]> sessionData = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== GC Tuning Solution ===\n");

        // Print GC configuration
        System.out.println("GC Configuration:");
        System.out.println("  Heap: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        System.out.println("  Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println();

        long startTime = System.currentTimeMillis();
        long maxPause = 0;
        int completed = 0;

        for (int i = 0; i < REQUESTS; i++) {
            long reqStart = System.currentTimeMillis();
            processRequest(i);
            long reqElapsed = System.currentTimeMillis() - reqStart;
            if (reqElapsed > maxPause) maxPause = reqElapsed;
            completed++;

            if (completed % 1000 == 0) {
                System.out.printf("  Completed %d requests (max single request: %d ms)%n",
                    completed, maxPause);
            }
        }

        long totalElapsed = System.currentTimeMillis() - startTime;
        System.out.printf("%nResults:%n");
        System.out.printf("  Total time: %d ms%n", totalElapsed);
        System.out.printf("  Throughput: %.1f req/s%n", (REQUESTS * 1000.0 / totalElapsed));
        System.out.printf("  Max single request: %d ms%n", maxPause);
    }

    static void processRequest(int id) {
        byte[] data = new byte[4096];
        sessionData.add(data);
        double result = 0;
        for (int i = 0; i < 100; i++) {
            result += Math.sin(i) * Math.cos(i);
        }
        if (sessionData.size() > 100) {
            sessionData.remove(0);
        }
    }
}
