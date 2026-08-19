package academy.javaengineering.jvm.profiling;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Exercise 3: Thread Contention Analysis
 *
 * Task: Create a contended scenario and analyze it with JFR.
 *
 * Run with: java -XX:StartFlightRecording=duration=30s,filename=recording.jfr Exercise3
 */
public class Exercise3 {

    private static final ReentrantLock lock = new ReentrantLock();
    private static long counter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Contention Analysis ===\n");

        // TODO: Create threads that contend on the lock
        // TODO: Analyze JFR recording for contention events
        Thread[] threads = new Thread[10];
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
        System.out.println("Counter: " + counter);
    }
}
