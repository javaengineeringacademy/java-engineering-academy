package academy.javaengineering.concurrency.sync.examples;

import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizationExamples {
    public static void main(String[] args) throws InterruptedException {
        // Example 1: Race condition without synchronization
        class UnsafeCounter {
            int count = 0;
            void increment() { count++; }
        }

        UnsafeCounter unsafe = new UnsafeCounter();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) unsafe.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Without sync: " + unsafe.count + " (expected 100000, likely less)");

        // Example 2: Fixed with synchronized
        class SafeCounter {
            int count = 0;
            synchronized void increment() { count++; }
        }

        SafeCounter safe = new SafeCounter();
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) safe.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("With synchronized: " + safe.count + " (always 100000)");

        // Example 3: Fixed with AtomicInteger
        AtomicInteger atomic = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) atomic.incrementAndGet();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("With AtomicInteger: " + atomic.get() + " (always 100000)");

        // Example 4: volatile flag
        class Worker implements Runnable {
            volatile boolean running = true;
            public void run() {
                while (running) { /* busy work */ }
                System.out.println("Worker stopped");
            }
            void stop() { running = false; }
        }

        Worker worker = new Worker();
        Thread wt = new Thread(worker);
        wt.start();
        Thread.sleep(100);
        worker.stop();
        wt.join();
    }
}
