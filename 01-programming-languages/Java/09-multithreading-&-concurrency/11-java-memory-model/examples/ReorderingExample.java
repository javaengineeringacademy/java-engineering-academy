package academy.javaengineering.concurrency.memorymodel.examples;

/**
 * Demonstrates instruction reordering by the compiler and CPU.
 * In concurrent programs, reordering can break assumptions about ordering
 * and visibility, leading to subtle bugs.
 */
public class ReorderingExample {

    // Classic reordering problem: the "broken" double-checked locking pattern
    static void reorderingBreaksDCL() {
        System.out.println("=== Reordering Breaks Double-Checked Locking ===");
        System.out.println("Without volatile, object construction can be reordered past reference publication.");

        class UnsafeSingleton {
            private static UnsafeSingleton instance;
            private int value;

            private UnsafeSingleton() {
                this.value = 42; // Step 2: initialize fields
            }

            static UnsafeSingleton getInstance() {
                if (instance == null) {
                    synchronized (UnsafeSingleton.class) {
                        if (instance == null) {
                            // What the JVM may actually do:
                            // Step 1: Allocate memory for the object
                            // Step 3: Assign reference to 'instance'
                            // Step 2: Initialize fields (reordered!)
                            //
                            // If Thread 2 reads 'instance' between steps 1 and 2,
                            // it sees a non-null reference but fields are not yet initialized.
                            instance = new UnsafeSingleton();
                        }
                    }
                }
                return instance;
            }
        }

        System.out.println("Unsafe DCL: instance = new UnsafeSingleton() can be reordered.");
        System.out.println("Without volatile, Thread 2 may see a non-null instance");
        System.out.println("with uninitialized fields.\n");
    }

    // Demonstrates compiler reordering
    static void compilerReordering() throws InterruptedException {
        System.out.println("=== Compiler Reordering ===");
        System.out.println("The compiler may reorder independent statements.");

        int[] a = {0};
        int[] b = {0};
        int[] reordered = {0};

        // Original intent:
        // a[0] = 1;
        // b[0] = 2;
        // reordered[0] = (a[0] == 1 && b[0] == 2) ? 1 : 0;
        //
        // Compiler might reorder to:
        // b[0] = 2;  // no dependency on a[0]
        // a[0] = 1;
        // reordered[0] = (a[0] == 1 && b[0] == 2) ? 1 : 0;
        //
        // In single-threaded code, the result is the same.
        // In concurrent code, another thread might see partial state.

        Thread writer = new Thread(() -> {
            a[0] = 1; // independent
            b[0] = 2; // independent — compiler may swap these
            reordered[0] = (a[0] == 1 && b[0] == 2) ? 1 : 0;
        });

        writer.start();
        writer.join();
        System.out.println("Compiler reordered independent writes.");
        System.out.println("In single-threaded code this is fine (as-if-serial).");
        System.out.println("In concurrent code, this can break visibility guarantees.\n");
    }

    // Demonstrates CPU out-of-order execution
    static void cpuReordering() throws InterruptedException {
        System.out.println("=== CPU Out-of-Order Execution ===");
        System.out.println("CPUs execute instructions out of order for performance.");

        int[] x = {0};
        int[] y = {0};
        int[] r1 = {0};
        int[] r2 = {0};

        // This is a classic "message passing" test.
        // If both reads see 0, it means the CPU reordered the stores.
        int iterations = 0;
        boolean seenBothZero = false;

        while (iterations < 100000 && !seenBothZero) {
            x[0] = 0;
            y[0] = 0;
            iterations++;

            Thread t1 = new Thread(() -> {
                x[0] = 1;           // store x
                r1[0] = y[0];       // load y
            });
            Thread t2 = new Thread(() -> {
                y[0] = 1;           // store y
                r2[0] = x[0];       // load x
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            if (r1[0] == 0 && r2[0] == 0) {
                seenBothZero = true;
                System.out.println("Iteration " + iterations + ": r1=" + r1[0] + ", r2=" + r2[0]);
                System.out.println("Both threads saw 0 — CPU reordered the stores!");
            }
        }

        if (!seenBothZero) {
            System.out.println("No reordering observed in " + iterations + " iterations.");
            System.out.println("(On x86, this is expected — x86 has a strong memory model)");
        }
        System.out.println("On ARM/weak-memory CPUs, this reordering is common.\n");
    }

    // Demonstrates memory reordering via store buffers
    static void memoryReordering() {
        System.out.println("=== Memory Reordering via Store Buffers ===");
        System.out.println();
        System.out.println("CPUs use store buffers to decouple writes from the cache:");
        System.out.println();
        System.out.println("  Thread 1 on Core 0:     Thread 2 on Core 1:");
        System.out.println("  ┌───────────────┐       ┌───────────────┐");
        System.out.println("  │  Execution     │       │  Execution     │");
        System.out.println("  │  Unit          │       │  Unit          │");
        System.out.println("  └───────┬───────┘       └───────┬───────┘");
        System.out.println("          │                        │");
        System.out.println("  ┌───────▼───────┐       ┌───────▼───────┐");
        System.out.println("  │ Store Buffer   │       │ Store Buffer   │");
        System.out.println("  └───────┬───────┘       └───────┬───────┘");
        System.out.println("          │                        │");
        System.out.println("  ┌───────▼───────┐       ┌───────▼───────┐");
        System.out.println("  │ L1 Cache       │◄─────►│ L1 Cache       │");
        System.out.println("  └───────────────┘       └───────────────┘");
        System.out.println();
        System.out.println("A write may sit in the store buffer and not yet be visible");
        System.out.println("to other cores, even if the instruction has 'committed'.");
        System.out.println("Memory barriers flush store buffers to ensure visibility.\n");
    }

    // Demonstrates why reordering happens
    static void whyReorderingHappens() {
        System.out.println("=== Why Reordering Happens ===");
        System.out.println();
        System.out.println("1. Instruction-Level Parallelism (ILP):");
        System.out.println("   CPUs execute multiple instructions simultaneously;");
        System.out.println("   reordering exposes more parallelism.\n");
        System.out.println("2. Cache Efficiency:");
        System.out.println("   Batching writes (store buffering) reduces cache");
        System.out.println("   coherence traffic.\n");
        System.out.println("3. Branch Prediction:");
        System.out.println("   Speculative execution may execute instructions that");
        System.out.println("   are later discarded.\n");
        System.out.println("4. Compiler Optimization:");
        System.out.println("   Eliminating redundant loads, hoisting invariants, etc.\n");
    }

    // How to prevent reordering
    static void howToPreventReordering() {
        System.out.println("=== How to Prevent Reordering ===");
        System.out.println();
        System.out.println("| Mechanism          | What It Prevents                                |");
        System.out.println("|--------------------|-------------------------------------------------|");
        System.out.println("| volatile write     | StoreStore + StoreLoad barriers                 |");
        System.out.println("| volatile read      | LoadLoad + LoadStore barriers                   |");
        System.out.println("| synchronized entry | LoadLoad + LoadStore (acquire semantics)        |");
        System.out.println("| synchronized exit  | StoreStore + LoadStore + StoreLoad (release)    |");
        System.out.println("| Unsafe.loadFence() | Load fence (JDK internal)                       |");
        System.out.println("| Unsafe.storeFence()| Store fence (JDK internal)                      |");
        System.out.println("| Unsafe.fullFence() | Full fence (JDK internal)                       |");
        System.out.println();
    }

    // Correct DCL with volatile
    static void correctDCL() throws InterruptedException {
        System.out.println("=== Correct DCL with volatile ===");

        class SafeSingleton {
            private static volatile SafeSingleton instance;
            private final int value;

            private SafeSingleton() {
                this.value = 42;
            }

            static SafeSingleton getInstance() {
                if (instance == null) {
                    synchronized (SafeSingleton.class) {
                        if (instance == null) {
                            instance = new SafeSingleton();
                            // volatile write prevents reordering:
                            // All field initializations happen-before
                            // the volatile write to 'instance'.
                        }
                    }
                }
                return instance;
            }
        }

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                SafeSingleton s = SafeSingleton.getInstance();
                System.out.println("Thread " + idx + ": value = " + s.value);
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("All threads see correct value thanks to volatile.\n");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          INSTRUCTION REORDERING IN THE JMM              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        reorderingBreaksDCL();
        compilerReordering();
        cpuReordering();
        memoryReordering();
        whyReorderingHappens();
        howToPreventReordering();
        correctDCL();

        System.out.println("Key takeaway: Reordering is invisible in single-threaded code");
        System.out.println("but can break assumptions in concurrent code. Use volatile, synchronized,");
        System.out.println("or final fields to prevent dangerous reorderings.");
    }
}
