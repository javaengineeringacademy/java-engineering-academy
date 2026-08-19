package academy.javaengineering.jvm.diagnostics;

/**
 * Solution 1: Thread Dump Analysis
 */
public class Solution1 {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Dump Analysis ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());

        // RUNNABLE thread
        new Thread(() -> {
            while (true) {
                double result = Math.sin(System.nanoTime());
            }
        }, "CPU-Worker").start();

        // WAITING thread
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    lock1.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Waiting-Thread").start();

        // TIMED_WAITING thread
        new Thread(() -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Sleeping-Thread").start();

        // BLOCKED thread (deadlock)
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lock2) { System.out.println("t1 acquired both locks"); }
            }
        }, "Deadlock-Thread-1");

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lock1) { System.out.println("t2 acquired both locks"); }
            }
        }, "Deadlock-Thread-2");

        t1.start();
        t2.start();

        System.out.println("\nThreads started. Capture thread dump:");
        System.out.println("  jstack " + ProcessHandle.current().pid());
        System.out.println("  Look for DEADLOCK section in output");
        Thread.sleep(60000);
    }
}
