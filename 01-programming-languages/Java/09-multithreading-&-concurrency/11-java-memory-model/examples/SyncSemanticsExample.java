package academy.javaengineering.concurrency.memorymodel.examples;

/**
 * Demonstrates synchronization semantics in detail:
 * - Mutual exclusion
 * - Memory visibility (happens-before)
 * - Reentrancy
 * - Lock acquisition and release ordering
 * - synchronized block vs method
 */
public class SyncSemanticsExample {

    // Mutual exclusion
    static void mutualExclusion() throws InterruptedException {
        System.out.println("=== Mutual Exclusion ===");
        System.out.println("synchronized ensures only one thread enters the critical section.");

        int[] counter = {0};
        Object lock = new Object();

        Runnable incrementer = () -> {
            for (int i = 0; i < 100000; i++) {
                synchronized (lock) {
                    counter[0]++;
                }
            }
        };

        Thread t1 = new Thread(incrementer);
        Thread t2 = new Thread(incrementer);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Expected: 200000, Actual: " + counter[0]);
        System.out.println("synchronized prevents lost updates.\n");
    }

    // Memory visibility through synchronized
    static void memoryVisibility() throws InterruptedException {
        System.out.println("=== Memory Visibility Through synchronized ===");
        System.out.println("All writes before unlock are visible to the next thread that acquires the same lock.");

        Object lock = new Object();
        int[] data = {0};
        boolean[] ready = {false};

        Thread writer = new Thread(() -> {
            synchronized (lock) {
                data[0] = 42;
                ready[0] = true;
            } // UNLOCK: all writes flushed
        });

        Thread reader = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lock) { // LOCK: sees all writes from previous unlock
                System.out.println("Reader: data=" + data[0] + ", ready=" + ready[0]);
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("The lock on the same monitor establishes happens-before.\n");
    }

    // Reentrancy
    static void reentrancy() throws InterruptedException {
        System.out.println("=== Reentrancy ===");
        System.out.println("A thread can re-acquire a lock it already holds (reentrant).");

        Object lock = new Object();

        class ReentrantCounter {
            int count = 0;

            synchronized void increment() {
                count++;           // holds lock
                decrementHelper(); // re-enters the same lock
            }

            synchronized void decrementHelper() {
                count--; // same thread re-acquires the lock — allowed
            }

            synchronized int getCount() {
                return count;
            }
        }

        ReentrantCounter counter = new ReentrantCounter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) counter.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Count: " + counter.getCount() + " (guaranteed 200000)");
        System.out.println("Reentrancy prevents deadlock when methods call other synchronized methods.\n");
    }

    // Lock acquisition and release ordering
    static void lockOrdering() throws InterruptedException {
        System.out.println("=== Lock Acquisition and Release Ordering ===");
        System.out.println("The happens-before is from previous unlock → current lock on the same monitor.");

        Object lockA = new Object();
        Object lockB = new Object();
        int[] dataFromA = {0};
        int[] dataFromB = {0};

        // Thread 1: locks A, writes data, unlocks A, then locks B, writes data, unlocks B
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                dataFromA[0] = 100;
            } // unlock A
            synchronized (lockB) {
                dataFromB[0] = 200;
            } // unlock B
        });

        // Thread 2: locks B, reads data (sees 200 from unlock B)
        //           then locks A, reads data (sees 100 from unlock A)
        Thread t2 = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lockB) {
                System.out.println("Thread 2 on lockB: dataFromB=" + dataFromB[0]);
            }
            synchronized (lockA) {
                System.out.println("Thread 2 on lockA: dataFromA=" + dataFromA[0]);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Each lock/unlock pair establishes its own happens-before.\n");
    }

    // synchronized block vs method
    static void blockVsMethod() throws InterruptedException {
        System.out.println("=== synchronized Block vs Method ===");
        System.out.println("Block: fine-grained control over what is synchronized.");
        System.out.println("Method: the entire method body is synchronized.\n");

        class Counter {
            int count = 0;

            // synchronized method — lock on 'this' for entire method
            synchronized void incrementMethod() {
                count++;
            }

            // equivalent to:
            void incrementEquivalent() {
                synchronized (this) { // explicit block
                    count++;
                }
            }

            // static synchronized — lock on Class object
            static int staticCount = 0;
            static synchronized void staticIncrement() {
                staticCount++;
            }

            synchronized int getCount() {
                return count;
            }
        }

        Counter counter = new Counter();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    counter.incrementMethod();
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("Count (synchronized method): " + counter.getCount());

        // Static synchronized
        Counter[] staticCounter = {new Counter()};
        Thread[] staticThreads = new Thread[10];
        for (int i = 0; i < staticThreads.length; i++) {
            staticThreads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    Counter.staticIncrement();
                }
            });
        }

        for (Thread t : staticThreads) t.start();
        for (Thread t : staticThreads) t.join();

        System.out.println("Static count (static synchronized): " + Counter.staticCount);
        System.out.println();
    }

    // Memory barriers in synchronized
    static void memoryBarriers() {
        System.out.println("=== Memory Barriers in synchronized ===");
        System.out.println();
        System.out.println("synchronized EXIT (release):");
        System.out.println("  StoreStore barrier — prevents reordering of prior stores");
        System.out.println("  StoreLoad barrier  — flushes all stores to memory");
        System.out.println();
        System.out.println("synchronized ENTRY (acquire):");
        System.out.println("  LoadLoad barrier   — prevents reordering of subsequent loads");
        System.out.println("  LoadStore barrier  — prevents reordering of subsequent stores");
        System.out.println();
        System.out.println("This means:");
        System.out.println("- All writes before synchronized EXIT are visible to the next synchronized ENTRY");
        System.out.println("- No reads/writes inside the critical section can be reordered before entry");
        System.out.println("- No reads/writes inside the critical section can be reordered after exit");
        System.out.println();
    }

    // Common pitfall: not synchronizing on the same monitor
    static void differentMonitorsPitfall() throws InterruptedException {
        System.out.println("=== Pitfall: Different Monitors ===");
        System.out.println("synchronized on DIFFERENT objects does NOT establish happens-before.\n");

        Object lock1 = new Object();
        Object lock2 = new Object();
        int[] data = {0};

        Thread writer = new Thread(() -> {
            synchronized (lock1) { // lock on lock1
                data[0] = 42;
            } // unlock lock1
        });

        Thread reader = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lock2) { // lock on lock2 — DIFFERENT monitor!
                // NO happens-before! May see stale value.
                System.out.println("Reader (lock2): data=" + data[0] + " (may NOT be 42)");
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Different monitors = no visibility guarantee.\n");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       SYNCHRONIZATION SEMANTICS — COMPLETE GUIDE        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        mutualExclusion();
        memoryVisibility();
        reentrancy();
        lockOrdering();
        blockVsMethod();
        memoryBarriers();
        differentMonitorsPitfall();

        System.out.println("Summary:");
        System.out.println("- synchronized provides mutual exclusion AND memory visibility");
        System.out.println("- Locks are reentrant (same thread can re-acquire)");
        System.out.println("- Happens-before is from unlock → next lock on the SAME monitor");
        System.out.println("- Block gives fine-grained control; method synchronizes the whole body");
    }
}
