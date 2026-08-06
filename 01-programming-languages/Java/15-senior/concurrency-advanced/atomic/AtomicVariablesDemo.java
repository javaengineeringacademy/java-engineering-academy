package academy.javaengineering.senior.concurrency;

import java.util.concurrent.atomic.*;

public class AtomicVariablesDemo {

    // ============================================================
    // 1. AtomicInteger
    // ============================================================

    public static void atomicIntegerDemo() throws Exception {
        System.out.println("=== AtomicInteger ===");

        AtomicInteger counter = new AtomicInteger(0);

        System.out.println("incrementAndGet: " + counter.incrementAndGet());
        System.out.println("decrementAndGet: " + counter.decrementAndGet());
        System.out.println("addAndGet(10): " + counter.addAndGet(10));
        System.out.println("getAndSet(5): " + counter.getAndSet(5));
        System.out.println("compareAndSet(5, 99): " + counter.compareAndSet(5, 99));
        System.out.println("getAndUpdate: " + counter.getAndUpdate(x -> x * 2));
        System.out.println("accumulateAndGet: " + counter.accumulateAndGet(10, Integer::sum));
    }

    // ============================================================
    // 2. AtomicLong
    // ============================================================

    public static void atomicLongDemo() throws Exception {
        System.out.println("\n=== AtomicLong ===");

        AtomicLong sequence = new AtomicLong(0);

        long next = sequence.incrementAndGet();
        System.out.println("Sequence: " + next);

        long current = sequence.get();
        System.out.println("Current: " + current);
    }

    // ============================================================
    // 3. AtomicBoolean
    // ============================================================

    public static void atomicBooleanDemo() throws Exception {
        System.out.println("\n=== AtomicBoolean ===");

        AtomicBoolean flag = new AtomicBoolean(false);

        boolean wasFalse = flag.compareAndSet(false, true);
        System.out.println("CAS false->true: " + wasFalse);

        boolean wasTrue = flag.compareAndSet(false, true);
        System.out.println("CAS false->true (again): " + wasTrue);

        System.out.println("Current: " + flag.get());
    }

    // ============================================================
    // 4. AtomicReference
    // ============================================================

    public static void atomicReferenceDemo() throws Exception {
        System.out.println("\n=== AtomicReference ===");

        AtomicReference<String> ref = new AtomicReference<>("initial");

        String old = ref.getAndUpdate(val -> val + "-updated");
        System.out.println("Old: " + old);
        System.out.println("New: " + ref.get());

        ref.set("reset");
        System.out.println("After set: " + ref.get());
    }

    // ============================================================
    // 5. AtomicStampedReference (ABA problem)
    // ============================================================

    public static void atomicStampedReferenceDemo() throws Exception {
        System.out.println("\n=== AtomicStampedReference ===");

        AtomicStampedReference<String> stampedRef =
            new AtomicStampedReference<>("A", 0);

        int[] stampHolder = new int[1];
        String value = stampedRef.get(stampHolder);
        int stamp = stampHolder[0];
        System.out.println("Value: " + value + ", Stamp: " + stamp);

        // CAS with stamp prevents ABA problem
        boolean success = stampedRef.compareAndSet("A", "B", stamp, stamp + 1);
        System.out.println("CAS A->B: " + success);

        value = stampedRef.get(stampHolder);
        System.out.println("After CAS: " + value + ", Stamp: " + stampHolder[0]);
    }

    // ============================================================
    // 6. LongAdder vs AtomicLong
    // ============================================================

    public static void longAdderVsAtomicLongDemo() throws Exception {
        System.out.println("\n=== LongAdder vs AtomicLong ===");

        int iterations = 1_000_000;
        int threadCount = 8;

        // AtomicLong
        AtomicLong atomicLong = new AtomicLong(0);
        Thread[] threads = new Thread[threadCount];
        long start = System.currentTimeMillis();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new Thread(() -> {
                for (int i = 0; i < iterations; i++) {
                    atomicLong.incrementAndGet();
                }
            });
            threads[t].start();
        }
        for (Thread th : threads) th.join();

        long atomicTime = System.currentTimeMillis() - start;
        System.out.println("AtomicLong result: " + atomicLong.get());
        System.out.println("AtomicLong time: " + atomicTime + "ms");

        // LongAdder
        LongAdder longAdder = new LongAdder();
        start = System.currentTimeMillis();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new Thread(() -> {
                for (int i = 0; i < iterations; i++) {
                    longAdder.increment();
                }
            });
            threads[t].start();
        }
        for (Thread th : threads) th.join();

        long adderTime = System.currentTimeMillis() - start;
        System.out.println("LongAdder result: " + longAdder.sum());
        System.out.println("LongAdder time: " + adderTime + "ms");
        System.out.println("Speedup: " + String.format("%.1fx", (double) atomicTime / adderTime));
    }

    // ============================================================
    // 7. Compare-And-Swap Explained
    // ============================================================

    public static void casExplained() throws Exception {
        System.out.println("\n=== Compare-And-Swap (CAS) ===");
        System.out.println("CAS is a CPU instruction: compare_and_swap(&value, expected, new_value)");
        System.out.println("It atomically: reads current value, compares with expected, writes new if match");
        System.out.println();

        AtomicInteger casDemo = new AtomicInteger(10);

        boolean success1 = casDemo.compareAndSet(10, 20);
        System.out.println("CAS(10 -> 20): " + success1 + " | Value: " + casDemo.get());

        boolean success2 = casDemo.compareAndSet(10, 30);
        System.out.println("CAS(10 -> 30): " + success2 + " | Value: " + casDemo.get());

        System.out.println();
        System.out.println("CAS properties:");
        System.out.println("- Atomic: read + compare + write in one step");
        System.out.println("- Lock-free: no blocking, spin on contention");
        System.out.println("- Used by: Atomic* classes, ConcurrentHashMap, StampedLock");
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) throws Exception {
        atomicIntegerDemo();
        atomicLongDemo();
        atomicBooleanDemo();
        atomicReferenceDemo();
        atomicStampedReferenceDemo();
        longAdderVsAtomicLongDemo();
        casExplained();
    }
}
