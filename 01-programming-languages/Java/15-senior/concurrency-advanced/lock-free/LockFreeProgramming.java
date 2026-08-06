import java.util.concurrent.atomic.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates lock-free programming concepts:
 * - Compare-And-Swap (CAS) operations
 * - AtomicInteger/Long internals
 * - ABA problem and solutions
 * - Lock-free queue and stack implementations
 */
public class LockFreeProgramming {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Lock-Free Programming ===\n");

        demonstrateCAS();
        demonstrateAtomicIntegerInternals();
        demonstrateABAProblem();
        demonstrateLockFreeQueue();
        demonstrateLockFreeStack();
        compareLockFreeVsLocked();

        System.out.println("\n=== All demonstrations completed ===");
    }

    /**
     * Compare-And-Swap (CAS) is an atomic operation that:
     * 1. Reads the current value
     * 2. Compares it with expected value
     * 3. If equal, swaps with new value
     * 4. Returns whether swap succeeded
     */
    private static void demonstrateCAS() throws InterruptedException {
        System.out.println("1. Compare-And-Swap (CAS) Operation:");
        System.out.println("   CAS is the foundation of lock-free algorithms.");

        AtomicInteger counter = new AtomicInteger(0);
        final int THREADS = 10;
        final int INCREMENTS = 10000;

        // CAS loop: keep trying until successful
        System.out.println("   Simulating CAS loop...");
        final AtomicInteger casAttempts = new AtomicInteger(0);

        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS; j++) {
                    int attempts = 0;
                    while (true) {
                        int current = counter.get();        // 1. Read current
                        int next = current + 1;            // 2. Calculate new
                        attempts++;
                        if (counter.compareAndSet(current, next)) {  // 3. CAS
                            casAttempts.addAndGet(attempts);
                            break;  // Success
                        }
                        // CAS failed (another thread modified), retry
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("   Final counter value: " + counter.get());
        System.out.println("   Expected: " + (THREADS * INCREMENTS));
        System.out.println("   Total CAS attempts: " + casAttempts.get() + "\n");
    }

    /**
     * AtomicInteger uses native CAS operations via Unsafe class.
     * Demonstrates various atomic operations.
     */
    private static void demonstrateAtomicIntegerInternals() throws InterruptedException {
        System.out.println("2. AtomicInteger Internals:");
        System.out.println("   AtomicInteger uses CAS via Unsafe.compareAndSwapInt().");

        AtomicInteger atomic = new AtomicInteger(0);

        // Various atomic operations
        atomic.set(10);                        // Set value
        System.out.println("   Initial value: " + atomic.get());

        int oldValue = atomic.getAndSet(20);   // Get old, set new
        System.out.println("   getAndSet returned: " + oldValue + ", now: " + atomic.get());

        int incremented = atomic.incrementAndGet();  // Increment and return
        System.out.println("   incrementAndGet: " + incremented);

        int added = atomic.addAndGet(5);       // Add and return
        System.out.println("   addAndGet(5): " + added);

        int expected = 26;
        int newValue = 30;
        boolean success = atomic.compareAndSet(expected, newValue);  // CAS
        System.out.println("   compareAndSet(" + expected + ", " + newValue + "): " + success);

        // Demonstrating CAS retry pattern
        System.out.println("\n   CAS retry pattern for thread-safe addition:");
        AtomicInteger sum = new AtomicInteger(0);
        final int[] values = {10, 20, 30, 40, 50};

        Thread[] adders = new Thread[values.length];
        for (int i = 0; i < values.length; i++) {
            final int val = values[i];
            adders[i] = new Thread(() -> {
                int attempts = 0;
                while (true) {
                    int current = sum.get();
                    int next = current + val;
                    attempts++;
                    if (sum.compareAndSet(current, next)) {
                        System.out.println("   Added " + val + " in " + attempts + " attempt(s)");
                        break;
                    }
                }
            });
            adders[i].start();
        }

        for (Thread t : adders) {
            t.join();
        }
        System.out.println("   Final sum: " + sum.get() + " (expected: 150)\n");
    }

    /**
     * ABA Problem: Value changes from A to B then back to A.
     * CAS sees "A" again and thinks nothing changed, but state may differ.
     */
    private static void demonstrateABAProblem() throws InterruptedException {
        System.out.println("3. ABA Problem:");
        System.out.println("   ABA occurs when value changes A -> B -> A.");

        // Simulate ABA problem
        AtomicReference<String> ref = new AtomicReference<>("A");

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Change A -> B
            ref.set("B");
            System.out.println("   Thread 1: Changed A -> B");
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Change B -> A
            ref.set("A");
            System.out.println("   Thread 2: Changed B -> A");
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("   Final value: " + ref.get());
        System.out.println("   CAS would succeed even though state changed!\n");

        // Solution 1: AtomicStampedReference (version number)
        System.out.println("   Solution 1: AtomicStampedReference (stamp/version)");
        AtomicStampedReference<String> stampedRef = new AtomicStampedReference<>("A", 0);
        int[] stampHolder = new int[1];

        String stampedValue = stampedRef.get(stampHolder);
        int stamp = stampHolder[0];
        System.out.println("   Initial: value=" + stampedValue + ", stamp=" + stamp);

        // CAS with stamp - must match both value AND stamp
        boolean casResult = stampedRef.compareAndSet("A", "B", stamp, stamp + 1);
        System.out.println("   CAS(A->B, stamp " + stamp + "->" + (stamp + 1) + "): " + casResult);

        // Solution 2: AtomicMarkableReference (boolean mark)
        System.out.println("\n   Solution 2: AtomicMarkableReference (mark bit)");
        AtomicMarkableReference<String> markableRef = new AtomicMarkableReference<>("A", false);
        boolean[] markHolder = new boolean[1];

        String markableValue = markableRef.get(markHolder);
        System.out.println("   Initial: value=" + markableValue + ", marked=" + markHolder[0]);

        boolean markResult = markableRef.compareAndSet("A", "B", false, true);
        System.out.println("   CAS(A->B, false->true): " + markResult + "\n");
    }

    /**
     * Lock-free queue using CAS operations.
     */
    private static void demonstrateLockFreeQueue() throws InterruptedException {
        System.out.println("4. Lock-Free Queue (ConcurrentLinkedQueue):");
        System.out.println("   Uses CAS for thread-safe enqueue/dequeue.");

        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        final int ITEMS = 100;
        final int PRODUCERS = 4;
        final int CONSUMERS = 4;

        CountDownLatch done = new CountDownLatch(1);
        final AtomicInteger produced = new AtomicInteger(0);
        final AtomicInteger consumed = new AtomicInteger(0);

        // Producers
        for (int i = 0; i < PRODUCERS; i++) {
            final int producerId = i;
            new Thread(() -> {
                for (int j = 0; j < ITEMS; j++) {
                    queue.offer(producerId * ITEMS + j);
                    produced.incrementAndGet();
                }
            }).start();
        }

        // Consumers
        for (int i = 0; i < CONSUMERS; i++) {
            new Thread(() -> {
                while (done.getCount() > 0 || !queue.isEmpty()) {
                    Integer item = queue.poll();
                    if (item != null) {
                        consumed.incrementAndGet();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

        // Wait for production to complete
        while (produced.get() < PRODUCERS * ITEMS) {
            Thread.sleep(10);
        }
        Thread.sleep(100);
        done.countDown();
        Thread.sleep(200);

        System.out.println("   Produced: " + produced.get());
        System.out.println("   Consumed: " + consumed.get());
        System.out.println("   Queue empty: " + queue.isEmpty() + "\n");
    }

    /**
     * Lock-free stack using CAS operations.
     */
    private static void demonstrateLockFreeStack() throws InterruptedException {
        System.out.println("5. Lock-Free Stack Implementation:");
        System.out.println("   Custom lock-free stack using AtomicReference.");

        LockFreeStack<Integer> stack = new LockFreeStack<>();
        final int ITEMS = 100;
        final int THREADS = 4;

        // Push items
        Thread[] pushers = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            final int threadId = i;
            pushers[i] = new Thread(() -> {
                for (int j = 0; j < ITEMS; j++) {
                    stack.push(threadId * ITEMS + j);
                }
            });
            pushers[i].start();
        }

        for (Thread t : pushers) {
            t.join();
        }

        System.out.println("   Stack size after pushes: " + stack.size());

        // Pop items
        final AtomicInteger popCount = new AtomicInteger(0);
        Thread[] poppers = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            poppers[i] = new Thread(() -> {
                while (true) {
                    Integer value = stack.pop();
                    if (value == null) break;
                    popCount.incrementAndGet();
                }
            });
            poppers[i].start();
        }

        for (Thread t : poppers) {
            t.join();
        }

        System.out.println("   Items popped: " + popCount.get());
        System.out.println("   Stack empty: " + stack.isEmpty() + "\n");
    }

    /**
     * Comparison of lock-free vs lock-based approaches.
     */
    private static void compareLockFreeVsLocked() throws InterruptedException {
        System.out.println("6. Lock-Free vs Lock-Based Performance:");
        System.out.println("   Comparing increment performance under contention.\n");

        final int ITERATIONS = 100000;
        final int THREADS = 8;

        // Lock-based counter
        Object counterLock = new Object();
        final int[] lockedCounter = {0};

        // Lock-free counter
        AtomicInteger atomicCounter = new AtomicInteger(0);

        // Warm up
        for (int i = 0; i < 1000; i++) {
            atomicCounter.incrementAndGet();
        }

        // Test lock-based
        long start = System.nanoTime();
        Thread[] lockedThreads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            lockedThreads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    synchronized (counterLock) {
                        lockedCounter[0]++;
                    }
                }
            });
            lockedThreads[i].start();
        }
        for (Thread t : lockedThreads) t.join();
        long lockedTime = System.nanoTime() - start;

        // Test lock-free
        start = System.nanoTime();
        Thread[] atomicThreads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    atomicCounter.incrementAndGet();
                }
            });
            atomicThreads[i].start();
        }
        for (Thread t : atomicThreads) t.join();
        long atomicTime = System.nanoTime() - start;

        System.out.println("   Lock-based (synchronized): " + lockedTime / 1_000_000 + " ms");
        System.out.println("   Lock-free (AtomicInteger): " + atomicTime / 1_000_000 + " ms");
        System.out.println("   Speedup: " + String.format("%.2f", (double) lockedTime / atomicTime) + "x");
    }

    /**
     * Lock-free stack implementation using CAS.
     */
    static class LockFreeStack<T> {
        private final AtomicReference<Node<T>> top = new AtomicReference<>();
        private final AtomicInteger size = new AtomicInteger(0);

        private static class Node<T> {
            final T value;
            Node<T> next;

            Node(T value) {
                this.value = value;
            }
        }

        void push(T value) {
            Node<T> newNode = new Node<>(value);
            Node<T> currentTop;
            do {
                currentTop = top.get();
                newNode.next = currentTop;
            } while (!top.compareAndSet(currentTop, newNode));
            size.incrementAndGet();
        }

        T pop() {
            Node<T> currentTop;
            Node<T> newTop;
            do {
                currentTop = top.get();
                if (currentTop == null) {
                    return null;
                }
                newTop = currentTop.next;
            } while (!top.compareAndSet(currentTop, newTop));

            size.decrementAndGet();
            return currentTop.value;
        }

        T peek() {
            Node<T> currentTop = top.get();
            return currentTop != null ? currentTop.value : null;
        }

        boolean isEmpty() {
            return top.get() == null;
        }

        int size() {
            return size.get();
        }
    }
}
