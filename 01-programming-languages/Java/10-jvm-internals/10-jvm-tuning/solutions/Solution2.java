package academy.javaengineering.jvm.tuning;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution 2: GC Latency Tuning
 */
public class Solution2 {

    private static final List<byte[]> sessions = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== GC Latency Tuning ===\n");
        System.out.println("Current config:");
        System.out.println("  Heap: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        System.out.println("  GC: " + System.getProperty("java.vm.info", "unknown") + "\n");

        long maxPause = 0;
        long totalPause = 0;
        int slowRequests = 0;

        for (int i = 0; i < 10000; i++) {
            long start = System.nanoTime();
            processRequest(i);
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (elapsed > maxPause) maxPause = elapsed;
            if (elapsed > 10) {
                totalPause += elapsed;
                slowRequests++;
            }
        }

        System.out.printf("Results:%n");
        System.out.printf("  Max single request: %d ms%n", maxPause);
        System.out.printf("  Slow requests (>10ms): %d%n", slowRequests);
        if (slowRequests > 0) {
            System.out.printf("  Avg slow request: %d ms%n", totalPause / slowRequests);
        }
        System.out.println("\nTuning tips:");
        System.out.println("  - If max pause > 50ms: try ZGC or reduce heap");
        System.out.println("  - If many slow requests: increase Young gen");
        System.out.println("  - If Full GC occurring: increase Old gen or reduce allocation");
    }

    static void processRequest(int id) {
        byte[] data = new byte[4096];
        if (id % 50 == 0) sessions.add(new byte[1024 * 100]);
        if (sessions.size() > 50) sessions.remove(0);
        double result = 0;
        for (int i = 0; i < 50; i++) result += Math.sin(i);
    }
}
