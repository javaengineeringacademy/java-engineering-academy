package academy.javaengineering.concurrency.sync.deeprdive;

/**
 * Classic Deadlock Example.
 *
 * Two threads, two locks, acquired in opposite order:
 *   - Thread A: locks lock1, then tries to acquire lock2
 *   - Thread B: locks lock2, then tries to acquire lock1
 *
 * If both threads acquire their first lock before the second,
 * each waits for the other → DEADLOCK.
 *
 * WARNING: This program WILL deadlock. Set a timeout or run with limited time.
 */
public class DeadlockExample {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        System.out.println("=== Classic Deadlock ===");
        System.out.println("Thread-A: will lock lock1, then try lock2");
        System.out.println("Thread-B: will lock lock2, then try lock1");
        System.out.println("If both acquire first lock before second → deadlock!\n");

        Thread threadA = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("[Thread-A] acquired lock1, sleeping 100ms...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("[Thread-A] trying to acquire lock2...");
                synchronized (lock2) {
                    System.out.println("[Thread-A] acquired lock2 (should not reach here)");
                }
            }
        }, "Thread-A");

        Thread threadB = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("[Thread-B] acquired lock2, sleeping 100ms...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("[Thread-B] trying to acquire lock1...");
                synchronized (lock1) {
                    System.out.println("[Thread-B] acquired lock1 (should not reach here)");
                }
            }
        }, "Thread-B");

        threadA.start();
        threadB.start();

        // Wait for both threads with a timeout to demonstrate deadlock
        try {
            threadA.join(3000);
            threadB.join(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Deadlock Detected ===");
        System.out.println("Both threads are stuck waiting for each other's lock.");
        System.out.println("Thread-A state: " + threadA.getState());
        System.out.println("Thread-B state: " + threadB.getState());

        if (threadA.isAlive() && threadB.isAlive()) {
            System.out.println("\nBoth threads still alive → DEADLOCK confirmed.");
            System.out.println("Forcing shutdown...");
            System.exit(0);
        }
    }
}
