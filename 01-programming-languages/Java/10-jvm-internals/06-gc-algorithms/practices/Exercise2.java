package academy.javaengineering.jvm.gc_algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: G1 GC Parameter Tuning
 *
 * Task: Tune G1 parameters to achieve < 50ms pause times for a
 * mixed workload of short-lived and long-lived objects.
 */
public class Exercise2 {

    private static final List<byte[]> longLived = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== G1 GC Tuning ===\n");
        System.out.println("Goal: Max pause time < 50ms\n");

        // TODO: Try different G1 parameters
        // java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 Exercise2
        // java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=16m Exercise2
        // java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:InitiatingHeapOccupancyPercent=30 Exercise2

        long maxPause = 0;
        for (int i = 0; i < 10000; i++) {
            long start = System.nanoTime();
            processRequest(i);
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (elapsed > maxPause) maxPause = elapsed;
        }

        System.out.printf("Max single request time: %d ms%n", maxPause);
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
