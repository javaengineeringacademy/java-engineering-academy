package academy.javaengineering.concurrency.memorymodel.examples;

/**
 * Demonstrates volatile semantics in detail:
 * - Visibility guarantee
 * - Ordering guarantee (memory barriers)
 * - What volatile does NOT guarantee (atomicity)
 * - volatile reference semantics
 */
public class VolatileSemanticsExample {

    // volatile ensures visibility
    static void visibilityGuarantee() throws InterruptedException {
        System.out.println("=== Volatile Visibility Guarantee ===");
        System.out.println("A volatile write is immediately visible to subsequent volatile reads.");

        volatile boolean[] ready = {false};
        int[] data = {0};

        Thread writer = new Thread(() -> {
            data[0] = 42;
            ready[0] = true; // volatile write — flushes to main memory
            System.out.println("Writer: data=" + data[0] + ", ready=true");
        });

        Thread reader = new Thread(() -> {
            while (!ready[0]) { /* volatile read — checks main memory */ }
            // ready volatile READ sees true
            // data write happens-before ready write (program order)
            // ready write happens-before ready read (volatile rule)
            // Therefore: data write happens-before ready read
            System.out.println("Reader: data=" + data[0] + " (guaranteed 42)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();
    }

    // volatile ordering guarantee (memory barriers)
    static void orderingGuarantee() throws InterruptedException {
        System.out.println("=== Volatile Ordering Guarantee ===");
        System.out.println("volatile prevents compiler/CPU from reordering past the volatile access.");

        int[] a = {0};
        int[] b = {0};
        volatile boolean[] flag = {false};

        Thread writer = new Thread(() -> {
            a[0] = 1;          // cannot be reordered past volatile write
            b[0] = 2;          // cannot be reordered past volatile write
            flag[0] = true;    // volatile write: StoreStore barrier before, StoreLoad barrier after
        });

        Thread reader = new Thread(() -> {
            while (!flag[0]) {}
            // volatile read: LoadLoad barrier after, LoadStore barrier after
            // a[0] and b[0] reads cannot be reordered before the volatile read
            System.out.println("Reader: a=" + a[0] + ", b=" + b[0] + " (guaranteed 1, 2)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();
    }

    // What volatile does NOT guarantee: atomicity
    static void atomicityLimitation() throws InterruptedException {
        System.out.println("=== Volatile Does NOT Guarantee Atomicity ===");
        System.out.println("volatile int count; count++ is NOT atomic (read-modify-write).");

        volatile int[] count = {0};
        int numIncrements = 100000;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < numIncrements; i++) count[0]++;
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < numIncrements; i++) count[0]++;
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        int expected = numIncrements * 2;
        System.out.println("Expected: " + expected + ", Actual: " + count[0]);
        System.out.println("Lost updates: " + (expected - count[0]));
        System.out.println("Use AtomicInteger for atomic increment.\n");
    }

    // volatile does NOT protect compound operations
    static void compoundOperationProblem() throws InterruptedException {
        System.out.println("=== Volatile Does NOT Protect Compound Operations ===");

        class Range {
            volatile int min = Integer.MAX_VALUE;
            volatile int max = Integer.MIN_VALUE;

            void update(int value) {
                // PROBLEM: another thread may read min/max between these lines
                min = Math.min(min, value); // non-atomic compound operation
                max = Math.max(max, value); // non-atomic compound operation
            }
        }

        Range range = new Range();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            final int val = i * 10;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    range.update(val + j);
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("min=" + range.min + ", max=" + range.max);
        System.out.println("(Values may be inconsistent because update() is not atomic)\n");
    }

    // volatile reference semantics
    static void referenceSemantics() throws InterruptedException {
        System.out.println("=== Volatile Reference Semantics ===");
        System.out.println("volatile on a reference guarantees the REFERENCE is visible,");
        System.out.println("but NOT necessarily the fields of the pointed-to object.");

        class Config {
            int value; // NOT volatile
            Config(int v) { this.value = v; }
        }

        volatile Config[] configHolder = {null};

        Thread writer = new Thread(() -> {
            Config c = new Config(999);
            configHolder[0] = c; // volatile write of reference
        });

        Thread reader = new Thread(() -> {
            while (configHolder[0] == null) {}
            Config local = configHolder[0]; // volatile read of reference
            // local.value is NOT guaranteed to be 999!
            // The reference is visible, but the object's fields may not be.
            System.out.println("Reader: config.value = " + local.value + " (may be 0 or 999)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Fix: make 'value' volatile, or use final fields.\n");
    }

    // Fix: volatile reference with final fields
    static void safeReferenceWithFinal() throws InterruptedException {
        System.out.println("=== Fix: Use final Fields for Safe Publication ===");

        class Config {
            final int value; // final — guaranteed visible after constructor
            Config(int v) { this.value = v; }
        }

        volatile Config[] configHolder = {null};

        Thread writer = new Thread(() -> {
            Config c = new Config(999);
            configHolder[0] = c;
        });

        Thread reader = new Thread(() -> {
            while (configHolder[0] == null) {}
            Config local = configHolder[0];
            // final field guarantee: value is guaranteed to be 999
            System.out.println("Reader: config.value = " + local.value + " (guaranteed 999)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();
    }

    // volatile for status flags (common pattern)
    static void statusFlagPattern() throws InterruptedException {
        System.out.println("=== Pattern: Volatile Status Flag ===");

        volatile boolean[] shutdownRequested = {false};

        Thread worker = new Thread(() -> {
            int iterations = 0;
            while (!shutdownRequested[0]) {
                iterations++;
                // do work
            }
            System.out.println("Worker: stopped after " + iterations + " iterations");
        });

        worker.start();
        Thread.sleep(100);
        shutdownRequested[0] = true; // volatile write signals shutdown
        worker.join();
        System.out.println("Worker stopped gracefully via volatile flag.\n");
    }

    // volatile for one-time initialization
    static void oneTimeInitPattern() throws InterruptedException {
        System.out.println("=== Pattern: Volatile One-Time Initialization ===");

        class ExpensiveResource {
            final int data;
            ExpensiveResource() {
                // simulate expensive initialization
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                this.data = 42;
            }
        }

        volatile ExpensiveResource[] resource = {null};

        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                if (resource[0] == null) {
                    synchronized (resource) {
                        if (resource[0] == null) {
                            resource[0] = new ExpensiveResource();
                            System.out.println("Thread " + idx + ": initialized resource");
                        }
                    }
                }
                System.out.println("Thread " + idx + ": data = " + resource[0].data);
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          VOLATILE SEMANTICS — COMPLETE GUIDE            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        visibilityGuarantee();
        orderingGuarantee();
        atomicityLimitation();
        compoundOperationProblem();
        referenceSemantics();
        safeReferenceWithFinal();
        statusFlagPattern();
        oneTimeInitPattern();

        System.out.println("Summary:");
        System.out.println("- volatile guarantees VISIBILITY of writes");
        System.out.println("- volatile guarantees ORDERING (prevents reordering)");
        System.out.println("- volatile does NOT guarantee ATOMICITY of compound operations");
        System.out.println("- volatile on a reference guarantees reference visibility, not field visibility");
    }
}
