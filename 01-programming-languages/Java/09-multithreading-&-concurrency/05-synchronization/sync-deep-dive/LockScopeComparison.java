package academy.javaengineering.concurrency.sync.deeprdive;

/**
 * Lock Scope Comparison.
 *
 * Same problem (thread-safe counter) solved three ways:
 *   1. Synchronized instance method (locks `this`)
 *   2. Static synchronized method (locks Class object)
 *   3. Synchronized block on private object
 *
 * Demonstrates which threads block on which locks and how scope differs.
 */
public class LockScopeComparison {

    // ─── Approach 1: Synchronized Instance Method ──────────────────────
    static class InstanceSyncCounter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized int getCount() {
            return count;
        }
    }

    // ─── Approach 2: Static Synchronized Method ────────────────────────
    static class StaticSyncCounter {
        private static int count = 0;

        public static synchronized void increment() {
            count++;
        }

        public static synchronized int getCount() {
            return count;
        }
    }

    // ─── Approach 3: Synchronized Block on Private Object ──────────────
    static class BlockSyncCounter {
        private int count = 0;
        private final Object lock = new Object();

        public void increment() {
            synchronized (lock) {
                count++;
            }
        }

        public int getCount() {
            synchronized (lock) {
                return count;
            }
        }
    }

    // ─── Shared Worker Logic ───────────────────────────────────────────
    @FunctionalInterface
    interface IncrementTask {
        void increment();
    }

    static void runTest(String testName, IncrementTask task1, IncrementTask task2,
                        Runnable getCount1, Runnable getCount2,
                        String label1, String label2) throws InterruptedException {
        System.out.println("=== " + testName + " ===");

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) task1.increment();
        }, "Worker-1-" + label1);

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) task2.increment();
        }, "Worker-2-" + label2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.print("  " + label1 + " result: ");
        getCount1.run();
        System.out.print("  " + label2 + " result: ");
        getCount2.run();
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        // ─── Test 1: Instance Sync — Two threads, SAME object ────────
        InstanceSyncCounter counter1a = new InstanceSyncCounter();
        runTest("Instance Sync — Same Object",
                counter1a::increment, counter1a::increment,
                () -> System.out.println(counter1a.getCount()),
                () -> System.out.println(counter1a.getCount()),
                "Thread-A (obj1)", "Thread-B (obj1)");
        System.out.println("  → Both threads lock on SAME `this` → serialized, count = 2000\n");

        // ─── Test 2: Instance Sync — Two threads, DIFFERENT objects ───
        InstanceSyncCounter counter2a = new InstanceSyncCounter();
        InstanceSyncCounter counter2b = new InstanceSyncCounter();
        runTest("Instance Sync — Different Objects",
                counter2a::increment, counter2b::increment,
                () -> System.out.println(counter2a.getCount()),
                () -> System.out.println(counter2b.getCount()),
                "Thread-A (obj1)", "Thread-B (obj2)");
        System.out.println("  → Different objects → independent locks → no serialization\n");

        // ─── Test 3: Static Sync — Two threads ──────────────────────
        StaticSyncCounter.count = 0; // reset static counter
        runTest("Static Sync — Global Lock",
                StaticSyncCounter::increment, StaticSyncCounter::increment,
                () -> System.out.println(StaticSyncCounter.getCount()),
                () -> System.out.println(StaticSyncCounter.getCount()),
                "Thread-A (static)", "Thread-B (static)");
        System.out.println("  → Both threads lock on Class object → serialized, count = 2000\n");

        // ─── Test 4: Block Sync — Same private lock ─────────────────
        BlockSyncCounter counter4 = new BlockSyncCounter();
        runTest("Block Sync — Same Private Lock",
                counter4::increment, counter4::increment,
                () -> System.out.println(counter4.getCount()),
                () -> System.out.println(counter4.getCount()),
                "Thread-A (lock1)", "Thread-B (lock1)");
        System.out.println("  → Both threads lock on same private object → serialized, count = 2000\n");

        // ─── Test 5: Block Sync — Different private locks ────────────
        BlockSyncCounter counter5a = new BlockSyncCounter();
        BlockSyncCounter counter5b = new BlockSyncCounter();
        runTest("Block Sync — Different Private Locks",
                counter5a::increment, counter5b::increment,
                () -> System.out.println(counter5a.getCount()),
                () -> System.out.println(counter5b.getCount()),
                "Thread-A (lockA)", "Thread-B (lockB)");
        System.out.println("  → Different private locks → independent → no serialization\n");

        System.out.println("=== Summary ===");
        System.out.println("Instance sync:    Per-object lock. Same object = serialized, different = independent.");
        System.out.println("Static sync:      Global lock (Class object). Always serialized across all instances.");
        System.out.println("Block sync:       Locks any specified object. Most flexible — fine-grained control.");
    }

    // Needed for StaticSyncCounter reset
    static class StaticSyncCounter {
        private static int count = 0;

        public static synchronized void increment() {
            count++;
        }

        public static synchronized int getCount() {
            return count;
        }
    }
}
