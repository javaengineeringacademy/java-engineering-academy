package academy.javaengineering.concurrency.communication.volatile;

/**
 * Demonstrates the Double-Checked Locking (DCL) pattern.
 *
 * DCL is used for lazy initialization of singletons.
 * Without volatile, DCL is BROKEN because the reference might be
 * published before the object is fully constructed.
 *
 * With volatile, DCL is safe because volatile ensures proper ordering.
 */
public class DoubleCheckedLocking {

    // ============================================
    // BROKEN DCL - Without volatile (UNSAFE!)
    // ============================================
    static class BrokenSingleton {
        // WITHOUT volatile - this is BROKEN!
        private static BrokenSingleton instance;

        private BrokenSingleton() {
            System.out.println("[BrokenSingleton] Constructor called");
        }

        static BrokenSingleton getInstance() {
            if (instance == null) {                    // First check (no lock)
                synchronized (BrokenSingleton.class) {
                    if (instance == null) {            // Second check (with lock)
                        instance = new BrokenSingleton();
                    }
                }
            }
            return instance;
        }
    }

    // ============================================
    // FIXED DCL - With volatile (SAFE!)
    // ============================================
    static class FixedSingleton {
        // WITH volatile - this is SAFE!
        private static volatile FixedSingleton instance;

        private FixedSingleton() {
            System.out.println("[FixedSingleton] Constructor called");
        }

        static FixedSingleton getInstance() {
            if (instance == null) {                    // First check (no lock)
                synchronized (FixedSingleton.class) {
                    if (instance == null) {            // Second check (with lock)
                        instance = new FixedSingleton();
                    }
                }
            }
            return instance;
        }
    }

    // ============================================
    // Demonstration
    // ============================================
    public static void main(String[] args) {
        System.out.println("=== Double-Checked Locking Pattern ===");
        System.out.println();

        // Demonstrate why DCL needs volatile
        System.out.println("--- Why DCL Without Volatile is Broken ---");
        System.out.println();
        System.out.println("The problem: Without volatile, object reference assignment");
        System.out.println("can be reordered with object construction:");
        System.out.println();
        System.out.println("  instance = new Singleton();");
        System.out.println();
        System.out.println("Is actually three steps:");
        System.out.println("  1. Allocate memory");
        System.out.println("  2. Initialize object fields");
        System.out.println("  3. Assign reference to 'instance'");
        System.out.println();
        System.out.println("Without volatile, steps 2 and 3 can be REORDERED!");
        System.out.println("Another thread might see 'instance != null' but read");
        System.out.println("uninitialized field values (like 0 or null).");
        System.out.println();

        // Demonstrate Fixed DCL
        System.out.println("--- Fixed DCL With Volatile ---");
        System.out.println();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            final int threadId = i + 1;
            threads[i] = new Thread(() -> {
                FixedSingleton s = FixedSingleton.getInstance();
                System.out.println("[Thread " + threadId + "] Got instance: " + s.hashCode());
            }, "Thread-" + threadId);
        }

        // Start all threads simultaneously
        for (Thread t : threads) {
            t.start();
        }

        // Wait for completion
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println();
        System.out.println("--- Summary ---");
        System.out.println();
        System.out.println("Without volatile (Broken DCL):");
        System.out.println("  - Object reference can be published before construction completes");
        System.out.println("  - Another thread might see a partially constructed object");
        System.out.println("  - This can cause subtle bugs that are hard to reproduce");
        System.out.println();
        System.out.println("With volatile (Fixed DCL):");
        System.out.println("  - Volatile enforces memory ordering (StoreLoad barrier)");
        System.out.println("  - Object construction completes before reference is published");
        System.out.println("  - All threads see the fully constructed object");
        System.out.println();
        System.out.println("In practice, prefer using:");
        System.out.println("  - Static holder pattern: class Holder { static final X x = new X(); }");
        System.out.println("  - Enum singleton: enum Singleton { INSTANCE; }");
        System.out.println("  - These are simpler and guaranteed safe by the JLS.");
    }
}
