package academy.javaengineering.jvm.gc_algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution 2: G1 GC Tuning
 */
public class Solution2 {

    private static final List<byte[]> longLived = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== G1 GC Tuning Solution ===\n");
        System.out.println("Recommended flags for < 50ms pauses:");
        System.out.println("  -XX:+UseG1GC");
        System.out.println("  -XX:MaxGCPauseMillis=50");
        System.out.println("  -XX:G1HeapRegionSize=8m");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=35");
        System.out.println("  -XX:G1NewSizePercent=30");
        System.out.println("  -XX:G1MaxNewSizePercent=50\n");

        long maxPause = 0;
        long totalPause = 0;
        int pauseCount = 0;

        for (int i = 0; i < 10000; i++) {
            long start = System.nanoTime();
            processRequest(i);
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (elapsed > maxPause) maxPause = elapsed;
            if (elapsed > 10) { // Count requests > 10ms
                totalPause += elapsed;
                pauseCount++;
            }
        }

        System.out.printf("Results:%n");
        System.out.printf("  Max single request: %d ms%n", maxPause);
        System.out.printf("  Slow requests (> 10ms): %d%n", pauseCount);
        if (pauseCount > 0) {
            System.out.printf("  Avg slow request: %d ms%n", totalPause / pauseCount);
        }
    }

    static void processRequest(int id) {
        byte[] data = new byte[4096];
        if (id % 100 == 0) {
            longLived.add(new byte[1024 * 100]);
        }
        double result = 0;
        for (int i = 0; i < 50; i++) result += Math.sin(i);
    }
}
