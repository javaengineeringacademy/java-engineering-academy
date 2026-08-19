package academy.javaengineering.jvm.profiling;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Solution 3: Thread Contention Analysis
 */
public class Solution3 {

    private static final ReentrantLock lock = new ReentrantLock();
    private static long counter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Contention Analysis ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Run: java -XX:StartFlightRecording=duration=30s,filename=recording.jfr " +
            ProcessHandle.current().pid() + "\n");

        Thread[] threads = new Thread[10];
        long start = System.currentTimeMillis();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    lock.lock();
                    try {
                        counter++;
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("Counter: %d, Time: %d ms%n", counter, elapsed);
        System.out.println("Open recording.jfr in JDK Mission Control to analyze contention.");
    }
}
