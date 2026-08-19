package academy.javaengineering.jvm.tuning;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: GC Latency Tuning
 *
 * Task: Tune GC parameters to achieve < 50ms pause times
 * while maintaining acceptable throughput.
 */
public class Exercise2 {

    private static final List<byte[]> sessions = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== GC Latency Tuning ===\n");

        // TODO: Try different configurations:
        // java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 Exercise2
        // java -XX:+UseZGC Exercise2
        // java -XX:+UseShenandoahGC Exercise2

        long maxPause = 0;
        for (int i = 0; i < 10000; i++) {
            long start = System.nanoTime();
            processRequest(i);
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (elapsed > maxPause) maxPause = elapsed;
        }

        System.out.printf("Max single request: %d ms%n", maxPause);
    }

    static void processRequest(int id) {
        byte[] data = new byte[4096];
        if (id % 50 == 0) sessions.add(new byte[1024 * 100]);
        if (sessions.size() > 50) sessions.remove(0);
        double result = 0;
        for (int i = 0; i < 50; i++) result += Math.sin(i);
    }
}
