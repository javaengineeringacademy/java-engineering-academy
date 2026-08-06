import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates the Java Memory Model's happens-before relationship.
 * The happens-before relationship guarantees memory visibility and ordering
 * of operations between threads.
 */
public class HappensBefore {

    // Shared mutable state
    private static int sharedValue = 0;
    private static boolean ready = false;
    private static volatile int volatileValue = 0;
    private static final Object lock = new Object();
    private static AtomicInteger atomicCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Happens-Before Relationship Rules ===\n");

        demonstrateProgramOrderRule();
        demonstrateMonitorLockRule();
        demonstrateVolatileVariableRule();
        demonstrateThreadStartRule();
        demonstrateThreadTerminationRule();
        demonstrateTransitivity();
        demonstrateHappensBeforeViolation();
        demonstrateRealWorldExample();

        System.out.println("\n=== All demonstrations completed ===");
    }

    /**
     * Rule 1: Program Order Rule
     * Within a single thread, each action happens-before every subsequent action
     * in program order (program order is the order in which statements appear).
     */
    private static void demonstrateProgramOrderRule() throws InterruptedException {
        System.out.println("1. Program Order Rule:");
        System.out.println("   Within a thread, operations execute in program order.");

        Thread thread = new Thread(() -> {
            // These operations happen-before each other in program order
            int a = 10;           // Action 1
            int b = 20;           // Action 2 (happens-after Action 1)
            int sum = a + b;      // Action 3 (happens-after Action 2)
            sharedValue = sum;    // Action 4 (happens-after Action 3)
            System.out.println("   Thread computed: " + sharedValue);
        });

        thread.start();
        thread.join();
        System.out.println("   Final value: " + sharedValue + "\n");
    }

    /**
     * Rule 2: Monitor Lock Rule
     * An unlock on a monitor happens-before every subsequent lock on that
     * same monitor. This ensures visibility of all writes before the unlock.
     */
    private static void demonstrateMonitorLockRule() throws InterruptedException {
        System.out.println("2. Monitor Lock Rule:");
        System.out.println("   Unlock happens-before next lock on same monitor.");

        final int[] result = new int[1];

        Thread writer = new Thread(() -> {
            synchronized (lock) {
                sharedValue = 42;  // Write under lock
                System.out.println("   Writer: set sharedValue = 42");
            }  // Unlock happens here
        });

        Thread reader = new Thread(() -> {
            try {
                Thread.sleep(50);  // Ensure writer releases lock first
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) {  // Lock happens after unlock
                result[0] = sharedValue;  // Guaranteed to see 42
                System.out.println("   Reader: read sharedValue = " + result[0]);
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("   Result: " + result[0] + "\n");
    }

    /**
     * Rule 3: Volatile Variable Rule
     * A write to a volatile field happens-before every subsequent read of
     * that same volatile field. This ensures visibility across threads.
     */
    private static void demonstrateVolatileVariableRule() throws InterruptedException {
        System.out.println("3. Volatile Variable Rule:");
        System.out.println("   Write to volatile happens-before read of volatile.");

        final int[] result = new int[1];

        Thread writer = new Thread(() -> {
            volatileValue = 100;  // Volatile write
            System.out.println("   Writer: set volatileValue = 100");
        });

        Thread reader = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            result[0] = volatileValue;  // Volatile read (guaranteed to see 100)
            System.out.println("   Reader: read volatileValue = " + result[0]);
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("   Result: " + result[0] + "\n");
    }

    /**
     * Rule 4: Thread Start Rule
     * A call to Thread.start() happens-before any action in the started thread.
     * This ensures that all writes before start() are visible to the new thread.
     */
    private static void demonstrateThreadStartRule() throws InterruptedException {
        System.out.println("4. Thread Start Rule:");
        System.out.println("   Thread.start() happens-before any action in started thread.");

        final int[] result = new int[1];
        sharedValue = 200;  // Write before start()

        Thread child = new Thread(() -> {
            // Guaranteed to see sharedValue = 200 due to Thread Start Rule
            result[0] = sharedValue;
            System.out.println("   Child thread read: " + result[0]);
        });

        System.out.println("   Parent wrote: " + sharedValue);
        child.start();
        child.join();
        System.out.println("   Result: " + result[0] + "\n");
    }

    /**
     * Rule 5: Thread Termination Rule
     * All actions in a thread happen-before any other thread detects that
     * this thread has terminated (via Thread.join() or Thread.isAlive()).
     */
    private static void demonstrateThreadTerminationRule() throws InterruptedException {
        System.out.println("5. Thread Termination Rule:");
        System.out.println("   Thread actions happen-before join() returns.");

        final int[] result = new int[1];

        Thread worker = new Thread(() -> {
            sharedValue = 300;  // Write in worker thread
            System.out.println("   Worker wrote: " + sharedValue);
        });

        worker.start();
        worker.join();  // After join() returns, all worker actions are visible

        result[0] = sharedValue;  // Guaranteed to see 300
        System.out.println("   After join(), parent reads: " + result[0] + "\n");
    }

    /**
     * Transitivity: If action A happens-before B, and B happens-before C,
     * then A happens-before C. This chains the happens-before relationships.
     */
    private static void demonstrateTransitivity() throws InterruptedException {
        System.out.println("6. Transitivity Rule:");
        System.out.println("   If A hb B and B hb C, then A hb C.");

        final int[] result = new int[1];

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                sharedValue = 500;  // Action A
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) {  // Action B: lock after t1's unlock
                volatileValue = sharedValue;  // Action C
                System.out.println("   T2 copied: " + sharedValue + " to volatile");
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            result[0] = volatileValue;  // Guaranteed to see 500
            System.out.println("   T3 read volatileValue: " + result[0]);
        });

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("   Final result: " + result[0] + "\n");
    }

    /**
     * Demonstrates what happens when happens-before relationship is violated.
     * Without proper synchronization, threads may see stale values.
     */
    private static void demonstrateHappensBeforeViolation() throws InterruptedException {
        System.out.println("7. Happens-Before Violation (without synchronization):");
        System.out.println("   Without happens-before, threads may see stale data.");

        // Reset shared state
        sharedValue = 0;
        ready = false;

        final int[] readValues = new int[10];
        final AtomicInteger readCount = new AtomicInteger(0);

        Thread writer = new Thread(() -> {
            sharedValue = 999;  // Write value
            ready = true;       // Signal ready (no volatile/synchronized)
            System.out.println("   Writer: set value=999, ready=true");
        });

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (ready) {
                    readValues[readCount.getAndIncrement()] = sharedValue;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        System.out.println("   Reader captured " + readCount.get() + " values");
        System.out.println("   (Without happens-before, may see partial updates)\n");
    }

    /**
     * Real-world example: Double-checked locking pattern
     * Shows how happens-before ensures correct lazy initialization.
     */
    private static void demonstrateRealWorldExample() throws InterruptedException {
        System.out.println("8. Real-World Example: Double-Checked Locking");

        // Demonstrate correct vs incorrect initialization
        LazySingleton correctInstance = LazySingleton.getInstance();
        LazySingleton sameInstance = LazySingleton.getInstance();

        System.out.println("   Same instance? " + (correctInstance == sameInstance));
        System.out.println("   Singleton hashCode: " + correctInstance.hashCode());

        // Demonstrate thread-safe counter
        ThreadSafeCounter counter = new ThreadSafeCounter();
        final int THREAD_COUNT = 10;
        final int INCREMENTS_PER_THREAD = 1000;

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        int expected = THREAD_COUNT * INCREMENTS_PER_THREAD;
        int actual = counter.getCount();
        System.out.println("   Expected count: " + expected);
        System.out.println("   Actual count: " + actual);
        System.out.println("   Counter correct? " + (expected == actual));
    }

    /**
     * Lazy Singleton with double-checked locking.
     * Uses volatile to ensure happens-before relationship.
     */
    static class LazySingleton {
        private static volatile LazySingleton instance;
        private final int data;

        private LazySingleton() {
            this.data = 42;
            System.out.println("   Singleton created with data: " + data);
        }

        static LazySingleton getInstance() {
            if (instance == null) {  // First check (no lock)
                synchronized (LazySingleton.class) {
                    if (instance == null) {  // Second check (with lock)
                        instance = new LazySingleton();
                    }
                }
            }
            return instance;
        }
    }

    /**
     * Thread-safe counter using synchronization.
     * Demonstrates happens-before guarantee with monitor lock.
     */
    static class ThreadSafeCounter {
        private int count = 0;
        private final Object counterLock = new Object();

        void increment() {
            synchronized (counterLock) {
                count++;
            }
        }

        int getCount() {
            synchronized (counterLock) {
                return count;
            }
        }
    }
}
