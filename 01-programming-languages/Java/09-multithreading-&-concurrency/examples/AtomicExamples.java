package academy.javaengineering.concurrency.examples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;

public class AtomicExamples {

    public static void main(String[] args) throws InterruptedException {
        example1_AtomicInteger();
        example2_AtomicReference();
        example3_AtomicStampedReference();
        example4_CASOperations();
        example5_LongAdderVsAtomicLong();
    }

    // Example 1: AtomicInteger
    static void example1_AtomicInteger() throws InterruptedException {
        System.out.println("=== Example 1: AtomicInteger ===");

        AtomicInteger counter = new AtomicInteger(0);

        // Atomic operations
        System.out.println("Initial value: " + counter.get());
        counter.set(10);
        System.out.println("After set(10): " + counter.get());

        counter.incrementAndGet();     // ++value
        System.out.println("After incrementAndGet: " + counter.get());

        counter.getAndIncrement();     // value++
        System.out.println("After getAndIncrement: " + counter.get());

        counter.addAndGet(5);          // value += 5
        System.out.println("After addAndGet(5): " + counter.get());

        counter.compareAndSet(15, 100); // if value == 15, set to 100
        System.out.println("After CAS(15->100): " + counter.get());

        counter.compareAndSet(100, 200);
        System.out.println("After CAS(100->200): " + counter.get());

        // Thread-safe increment test
        AtomicInteger atomicCount = new AtomicInteger(0);
        Runnable incrementTask = () -> {
            for (int i = 0; i < 1000; i++) {
                atomicCount.incrementAndGet();
            }
        };

        Thread t1 = new Thread(incrementTask);
        Thread t2 = new Thread(incrementTask);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Atomic counter after 2 threads x 1000: " + atomicCount.get());

        // Non-atomic counter for comparison
        int[] nonAtomic = {0};
        Runnable nonAtomicTask = () -> {
            for (int i = 0; i < 1000; i++) {
                nonAtomic[0]++; // NOT thread-safe!
            }
        };

        Thread t3 = new Thread(nonAtomicTask);
        Thread t4 = new Thread(nonAtomicTask);
        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println("Non-atomic counter after 2 threads x 1000: " + nonAtomic[0] + " (may not be 2000!)");

        System.out.println();
    }

    // Example 2: AtomicReference
    static void example2_AtomicReference() throws InterruptedException {
        System.out.println("=== Example 2: AtomicReference ===");

        AtomicReference<String> ref = new AtomicReference<>("initial");

        System.out.println("Initial: " + ref.get());

        // compareAndSet
        boolean success = ref.compareAndSet("initial", "updated");
        System.out.println("CAS success: " + success + ", value: " + ref.get());

        // Try to CAS with wrong expected value
        success = ref.compareAndSet("initial", "failed");
        System.out.println("CAS with wrong expected - success: " + success + ", value: " + ref.get());

        // getAndSet
        String oldValue = ref.getAndSet("replaced");
        System.out.println("Old value: " + oldValue + ", new value: " + ref.get());

        // Thread-safe reference swap
        AtomicReference<int[]> sharedRef = new AtomicReference<>(new int[]{0, 0});

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                sharedRef.set(new int[]{i, i * 2});
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                int[] current = sharedRef.get();
                // Process with current reference
                if (current != null) {
                    // Atomic operations on reference
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final reference: [" + sharedRef.get()[0] + ", " + sharedRef.get()[1] + "]");

        System.out.println();
    }

    // Example 3: AtomicStampedReference (ABA problem solution)
    static void example3_AtomicStampedReference() throws InterruptedException {
        System.out.println("=== Example 3: AtomicStampedReference (ABA Problem) ===");

        // ABA Problem: Thread reads A, gets preempted, another thread changes A->B->A,
        // first thread CAS succeeds incorrectly because value looks unchanged

        AtomicStampedReference<String> stampedRef = new AtomicStampedReference<>("A", 0);

        int[] stampHolder = new int[1];
        String value = stampedRef.get(stampHolder);
        int initialStamp = stampHolder[0];

        System.out.println("Initial: " + value + " (stamp=" + initialStamp + ")");

        // Simulate ABA problem
        // Another thread changes A -> B -> A
        Thread modifier = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                stampedRef.set("B", 1); // A -> B
                stampedRef.set("A", 2); // B -> A
                System.out.println("Modifier: A -> B -> A");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread casThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
                // Without stamp, CAS would succeed (ABA problem!)
                boolean success = stampedRef.compareAndSet("A", "C", initialStamp, initialStamp + 1);
                System.out.println("CAS with old stamp: " + success + " (stamp used=" + initialStamp + ")");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        modifier.start();
        casThread.start();
        modifier.join();
        casThread.join();

        // Check current state
        value = stampedRef.get(stampHolder);
        System.out.println("Final: " + value + " (stamp=" + stampHolder[0] + ")");

        // Correct approach with stamp
        AtomicStampedReference<String> correctRef = new AtomicStampedReference<>("X", 0);
        int[] stamp = new int[1];
        String val = correctRef.get(stamp);
        System.out.println("\nCorrect approach - Current: " + val + " stamp=" + stamp[0]);

        // Use current stamp for CAS
        boolean correctCas = correctRef.compareAndSet("X", "Y", stamp[0], stamp[0] + 1);
        System.out.println("Correct CAS: " + correctCas);
        val = correctRef.get(stamp);
        System.out.println("After CAS: " + val + " stamp=" + stamp[0]);

        System.out.println();
    }

    // Example 4: CAS (Compare-And-Swap) operations
    static void example4_CASOperations() throws InterruptedException {
        System.out.println("=== Example 4: CAS Operations ===");

        AtomicInteger casCounter = new AtomicInteger(0);

        // CAS is the foundation of lock-free algorithms
        // It atomically: if (value == expected) { value = newValue; return true; }

        // demonstrate CAS loop
        Runnable casTask = () -> {
            while (true) {
                int current = casCounter.get();
                int newValue = current + 1;
                if (casCounter.compareAndSet(current, newValue)) {
                    break; // CAS succeeded
                }
                // CAS failed, retry (another thread modified the value)
            }
        };

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(casTask);
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        System.out.println("CAS counter after 10 threads: " + casCounter.get());

        // getAndUpdate with CAS
        AtomicInteger value = new AtomicInteger(10);
        int oldVal = value.getAndUpdate(x -> x * 2);
        System.out.println("Old value: " + oldVal + ", new: " + value.get());

        int updatedVal = value.accumulateAndGet(5, Integer::sum);
        System.out.println("After accumulateAndGet(+5): " + updatedVal);

        // updateAndGet with retry logic
        AtomicInteger tricky = new AtomicInteger(0);
        tricky.updateAndGet(x -> x < 100 ? x + 10 : x);
        System.out.println("After conditional update: " + tricky.get());

        System.out.println();
    }

    // Example 5: LongAdder vs AtomicLong
    static void example5_LongAdderVsAtomicLong() throws InterruptedException {
        System.out.println("=== Example 5: LongAdder vs AtomicLong ===");

        // AtomicLong - single variable, high contention
        AtomicLong atomicLong = new AtomicLong(0);

        // LongAdder - spread across cells, better for high contention
        java.util.concurrent.atomic.LongAdder longAdder = new java.util.concurrent.atomic.LongAdder();

        int numThreads = 8;
        int incrementsPerThread = 100000;

        // Benchmark AtomicLong
        long start = System.nanoTime();
        Thread[] atomicThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    atomicLong.incrementAndGet();
                }
            });
            atomicThreads[i].start();
        }
        for (Thread t : atomicThreads) t.join();
        long atomicTime = System.nanoTime() - start;

        // Benchmark LongAdder
        start = System.nanoTime();
        Thread[] adderThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            adderThreads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    longAdder.increment();
                }
            });
            adderThreads[i].start();
        }
        for (Thread t : adderThreads) t.join();
        long adderTime = System.nanoTime() - start;

        System.out.println("AtomicLong final: " + atomicLong.get() +
                " (time: " + atomicTime / 1_000_000 + "ms)");
        System.out.println("LongAdder final: " + longAdder.sum() +
                " (time: " + adderTime / 1_000_000 + "ms)");
        System.out.println("LongAdder is faster under high contention");
        System.out.println("Note: LongAdder.sum() is not atomic, use for statistics");

        System.out.println();
    }
}
