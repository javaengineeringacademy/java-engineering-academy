package academy.javaengineering.concurrency.sync.deeprdive;

/**
 * Synchronized Block Example.
 *
 * `synchronized(lockObject) { ... }` locks on any explicit object.
 * This gives fine-grained control over what exactly is locked.
 *
 * Key advantages:
 * - Lock on a private object (prevents external code from interfering)
 * - Lock on different objects for different purposes
 * - Reduce scope of synchronization to only what's needed
 */
public class SyncBlockExample {

    private int count = 0;
    private final Object countLock = new Object(); // private lock object
    private final Object logLock = new Object();   // separate lock for logging

    private int logCount = 0;

    /**
     * Uses a private lock object — external code cannot interfere.
     */
    public void increment() {
        synchronized (countLock) {
            String threadName = Thread.currentThread().getName();
            System.out.println("[" + threadName + "] increment — count before: " + count);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            count++;
            System.out.println("[" + threadName + "] increment — count after: " + count);
        }
    }

    /**
     * Uses a DIFFERENT lock object — independent from countLock.
     * Threads can do logging concurrently with incrementing.
     */
    public void logAction(String action) {
        synchronized (logLock) {
            String threadName = Thread.currentThread().getName();
            logCount++;
            System.out.println("[" + threadName + "] log #" + logCount + ": " + action);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Locking on `this` — same as synchronized instance method.
     */
    public void incrementUsingThis() {
        synchronized (this) {
            count++;
        }
    }

    public int getCount() {
        return count;
    }

    public int getLogCount() {
        return logCount;
    }

    public static void main(String[] args) throws InterruptedException {
        SyncBlockExample example = new SyncBlockExample();

        // --- Scenario 1: Two threads, same lock (countLock) ---
        System.out.println("=== Scenario 1: Same Lock (countLock) ===");
        Thread t1 = new Thread(example::increment, "Thread-A");
        Thread t2 = new Thread(example::increment, "Thread-B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final count: " + example.getCount());
        System.out.println("Both threads serialized on countLock.\n");

        // --- Scenario 2: Two threads, different locks (countLock vs logLock) ---
        System.out.println("=== Scenario 2: Different Locks (countLock vs logLock) ===");
        Thread t3 = new Thread(example::increment, "Thread-C");
        Thread t4 = new Thread(() -> example.logAction("user login"), "Thread-D");

        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println("count: " + example.getCount() + ", logCount: " + example.getLogCount());
        System.out.println("Different locks — increment and log ran independently.\n");

        // --- Scenario 3: Synchronized block on `this` ---
        System.out.println("=== Scenario 3: Synchronized Block on `this` ===");
        Thread t5 = new Thread(example::incrementUsingThis, "Thread-E");
        Thread t6 = new Thread(example::incrementUsingThis, "Thread-F");

        t5.start();
        t6.start();
        t5.join();
        t6.join();

        System.out.println("Final count: " + example.getCount());
        System.out.println("synchronized(this) behaves identically to synchronized method.");

        // --- Scenario 4: Locking on a String literal (NOT recommended) ---
        System.out.println("\n=== Scenario 4: Locking on String Literal (Anti-pattern) ===");
        Thread t7 = new Thread(() -> {
            synchronized ("shared") {
                System.out.println("[Thread-G] acquired string lock");
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Thread-G");
        Thread t8 = new Thread(() -> {
            synchronized ("shared") {
                System.out.println("[Thread-H] acquired string lock");
            }
        }, "Thread-H");

        t7.start();
        t8.start();
        t7.join();
        t8.join();

        System.out.println("String literals are interned — all code using same literal shares the lock.");
        System.out.println("This is dangerous: external code could also lock on the same string.");
    }
}
