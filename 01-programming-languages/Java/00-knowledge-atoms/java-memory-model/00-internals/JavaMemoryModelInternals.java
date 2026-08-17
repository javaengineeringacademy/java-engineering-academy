package academy.javaengineering.knowledgeatoms.memorymodel;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaMemoryModelInternals {

    private static volatile boolean running = true;
    private static int nonVolatileCounter = 0;
    private static volatile int volatileCounter = 0;
    private static final Object lock = new Object();
    private static int synchronizedCounter = 0;
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Java Memory Model Internals ===\n");

        // 1. Happens-before relationships
        System.out.println("--- Happens-Before Rules ---");
        System.out.println("1. Program Order: actions in same thread are ordered");
        System.out.println("2. Monitor Lock: unlock happens-before next lock");
        System.out.println("3. Volatile Variable: write happens-before read");
        System.out.println("4. Thread Start: start() happens-before actions in thread");
        System.out.println("5. Thread Join: join() returns after all actions complete");
        System.out.println("6. Transitivity: A hb B and B hb C => A hb C");

        // 2. Volatile semantics
        System.out.println("\n--- Volatile Semantics ---");
        demonstrateVolatile();

        // 3. Synchronized vs volatile
        System.out.println("\n--- Synchronized vs Volatile ---");
        demonstrateSynchronized();

        // 4. Safe publication
        System.out.println("\n--- Safe Publication Patterns ---");
        demonstrateSafePublication();
    }

    private static void demonstrateVolatile() throws InterruptedException {
        // Volatile guarantees visibility but not atomicity
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                volatileCounter++;  // NOT atomic: read, add, write
            }
            running = false;  // volatile write — visible to reader
        });

        Thread reader = new Thread(() -> {
            while (running) {
                // volatile read — always sees latest value
            }
            System.out.println("Volatile counter (may be < 1000): " + volatileCounter);
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        System.out.println("volatile provides visibility, not atomicity");
        System.out.println("Use AtomicInteger for atomic operations");
    }

    private static void demonstrateSynchronized() throws InterruptedException {
        nonVolatileCounter = 0;
        synchronizedCounter = 0;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                nonVolatileCounter++;  // race condition
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronized (lock) {
                    synchronizedCounter++;  // thread-safe
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Non-volatile counter (race condition): " + nonVolatileCounter);
        System.out.println("Synchronized counter (correct): " + synchronizedCounter);
        System.out.println("Atomic counter (correct): " + atomicCounter.addAndGet(1000));
    }

    private static void demonstrateSafePublication() {
        // Unsafe: publishing this in constructor
        System.out.println("Unsafe publication: 'this' escapes before constructor completes");
        System.out.println("Other thread may see partially constructed object");

        // Safe: using volatile reference
        System.out.println("\nSafe publication mechanisms:");
        System.out.println("  1. volatile write/read");
        System.out.println("  2. synchronized unlock/lock");
        System.out.println("  3. final fields (JMM guarantee)");
        System.out.println("  4. static initializer");
        System.out.println("  5. concurrent collections");

        // Double-checked locking with volatile
        System.out.println("\nDouble-checked locking requires volatile:");
        System.out.println("  private static volatile Singleton instance;");
        System.out.println("  Without volatile, instance may be seen partially constructed");
    }
}
