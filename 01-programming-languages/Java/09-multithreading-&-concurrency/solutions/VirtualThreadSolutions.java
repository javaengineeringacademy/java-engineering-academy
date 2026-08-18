package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VirtualThreadSolutions {

    public static void main(String[] args) throws Exception {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Create virtual thread
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Virtual Thread Basics ===");

        Thread platformThread = new Thread(() -> {
            System.out.println("Platform thread: " + Thread.currentThread());
        });

        Thread virtualThread = Thread.ofVirtual().name("my-virtual-thread").start(() -> {
            System.out.println("Virtual thread: " + Thread.currentThread());
        });

        platformThread.start();
        platformThread.join();
        virtualThread.join();
    }

    /**
     * Exercise 2: Virtual thread with ExecutorService
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Virtual Thread Executor ===");
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < 100; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                System.out.println("Task " + taskNum + " in " + Thread.currentThread());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("All 100 tasks completed");
    }

    /**
     * Exercise 3: Virtual thread pinning
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Virtual Thread Pinning ===");
        Object lock = new Object();
        long[] syncTime = {0};
        long[] lockTime = {0};

        long start = System.currentTimeMillis();
        Thread[] syncThreads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            syncThreads[i] = Thread.ofVirtual().start(() -> {
                synchronized (lock) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        for (Thread t : syncThreads) t.join();
        syncTime[0] = System.currentTimeMillis() - start;
        System.out.println("Synchronized time: " + syncTime[0] + "ms");

        start = System.currentTimeMillis();
        var reentrantLock = new java.util.concurrent.locks.ReentrantLock();
        Thread[] lockThreads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            lockThreads[i] = Thread.ofVirtual().start(() -> {
                reentrantLock.lock();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    reentrantLock.unlock();
                }
            });
        }
        for (Thread t : lockThreads) t.join();
        lockTime[0] = System.currentTimeMillis() - start;
        System.out.println("ReentrantLock time: " + lockTime[0] + "ms");
    }

    /**
     * Exercise 4: Structured concurrency (simplified without preview features)
     */
    static void exercise4() throws Exception {
        System.out.println("=== Exercise 4: Structured Concurrency ===");
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        try {
            var future1 = executor.submit(() -> {
                Thread.sleep(1000);
                return "Data from Service A";
            });

            var future2 = executor.submit(() -> {
                Thread.sleep(800);
                return "Data from Service B";
            });

            String result1 = future1.get();
            String result2 = future2.get();

            System.out.println("Combined: " + result1 + " + " + result2);
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    /**
     * Exercise 5: Virtual thread with I/O
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Virtual Thread I/O ===");
        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < 1000; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (taskNum % 200 == 0) {
                    System.out.println("Task " + taskNum + " completed");
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("1000 virtual threads completed in " + elapsed + "ms");
    }
}
