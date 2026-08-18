package academy.javaengineering.concurrency.memorymodel.internals;

/**
 * Deep dive into happens-before internals.
 * Demonstrates memory barriers, transitivity chains, and the JMM's formal model.
 */
public class HappensBeforeInternals {

    // ═══════════════════════════════════════════════════════════════
    // 1. Happens-Before as a Partial Ordering
    // ═══════════════════════════════════════════════════════════════
    // The JMM defines happens-before as a partial order on actions.
    // Not all actions are ordered — only those connected by happens-before edges.
    // Actions NOT connected by happens-before may be observed in any order.

    static void partialOrdering() throws InterruptedException {
        System.out.println("=== Happens-Before as Partial Ordering ===");
        System.out.println("Not all actions are ordered. Only those connected by happens-before edges.");
        System.out.println();

        // Action A: x = 1 (Thread 1)
        // Action B: y = 2 (Thread 1)
        // Action C: r1 = y (Thread 2)
        // Action D: r2 = x (Thread 2)
        //
        // A HB B (program order in Thread 1)
        // C HB D (program order in Thread 2)
        //
        // But there is NO happens-before between {A,B} and {C,D}!
        // So r1 and r2 may see any combination of values.

        int[] x = {0};
        int[] y = {0};
        int[] r1 = {0};
        int[] r2 = {0};

        Thread t1 = new Thread(() -> {
            x[0] = 1; // Action A
            y[0] = 2; // Action B — A HB B
        });

        Thread t2 = new Thread(() -> {
            r1[0] = y[0]; // Action C
            r2[0] = x[0]; // Action D — C HB D
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Thread 1 wrote: x=1, y=2");
        System.out.println("Thread 2 read:  y=" + r1[0] + ", x=" + r2[0]);
        System.out.println("Possible results: (y=0,x=0), (y=2,x=0), (y=2,x=1)");
        System.out.println("Without volatile/synchronized, there is no HB between threads.\n");
    }

    // ═══════════════════════════════════════════════════════════════
    // 2. Transitivity Chains
    // ═══════════════════════════════════════════════════════════════
    // Transitivity allows chaining happens-before relationships.
    // This is how multi-thread pipelines establish visibility.

    static void transitivityChain() throws InterruptedException {
        System.out.println("=== Transitivity Chain ===");
        System.out.println("If A HB B and B HB C, then A HB C.");

        // Thread 1 → Thread 2 → Thread 3
        // Thread 1 writes data, sets flag1 (volatile)
        // Thread 2 sees flag1, processes data, sets flag2 (volatile)
        // Thread 3 sees flag2, reads processed data
        //
        // Chain: write data HB write flag1 HB read flag1 HB process HB write flag2 HB read flag2
        // By transitivity: write data HB read flag2

        volatile boolean[] flag1 = {false};
        volatile boolean[] flag2 = {false};
        int[] rawData = {0};
        int[] processedData = {0};

        // Stage 1: Producer
        Thread producer = new Thread(() -> {
            rawData[0] = 100;        // write data
            flag1[0] = true;         // volatile write (flag1)
        });

        // Stage 2: Processor
        Thread processor = new Thread(() -> {
            while (!flag1[0]) {}     // volatile read (flag1)
            processedData[0] = rawData[0] * 2;  // process
            flag2[0] = true;         // volatile write (flag2)
        });

        // Stage 3: Consumer
        Thread consumer = new Thread(() -> {
            while (!flag2[0]) {}     // volatile read (flag2)
            // By transitivity: rawData[0] write HB flag1 write HB flag1 read
            //   HB processedData write HB flag2 write HB flag2 read
            // Therefore: processedData[0] is guaranteed to be 200
            System.out.println("Consumer sees processedData: " + processedData[0] + " (guaranteed 200)");
        });

        producer.start();
        processor.start();
        consumer.start();
        producer.join();
        processor.join();
        consumer.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 3. Memory Barrier Types
    // ═══════════════════════════════════════════════════════════════
    // The JMM maps to hardware memory barriers. Understanding these
    // explains WHY volatile and synchronized work.

    static void memoryBarrierTypes() {
        System.out.println("=== Memory Barrier Types ===");
        System.out.println();
        System.out.println("| Barrier      | Prevents Reordering Of                        |");
        System.out.println("|--------------|------------------------------------------------|");
        System.out.println("| LoadLoad     | Load before Load                               |");
        System.out.println("| StoreStore   | Store before Store                             |");
        System.out.println("| LoadStore    | Load before Store                              |");
        System.out.println("| StoreLoad    | Store before Load (full barrier, most expensive)|");
        System.out.println();
        System.out.println("How volatile maps to barriers:");
        System.out.println();
        System.out.println("volatile WRITE:");
        System.out.println("  StoreStore barrier  ← prevents prior stores from moving past the volatile write");
        System.out.println("  [volatile write]");
        System.out.println("  StoreLoad barrier   ← prevents the volatile write from moving past subsequent loads");
        System.out.println();
        System.out.println("volatile READ:");
        System.out.println("  LoadLoad barrier    ← prevents the volatile read from moving past subsequent loads");
        System.out.println("  [volatile read]");
        System.out.println("  LoadStore barrier   ← prevents subsequent stores from moving before the volatile read");
        System.out.println();
        System.out.println("synchronized ENTRY (acquire):");
        System.out.println("  LoadLoad barrier");
        System.out.println("  LoadStore barrier");
        System.out.println();
        System.out.println("synchronized EXIT (release):");
        System.out.println("  StoreStore barrier");
        System.out.println("  LoadStore barrier");
        System.out.println("  StoreLoad barrier");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 4. Final Field Guarantee — Deep Dive
    // ═══════════════════════════════════════════════════════════════
    // The final field guarantee is one of the most powerful (and least
    // understood) features of the JMM.

    static void finalFieldGuarantee() throws InterruptedException {
        System.out.println("=== Final Field Guarantee — Deep Dive ===");
        System.out.println();
        System.out.println("The JMM guarantees that after a constructor completes:");
        System.out.println("- All final fields are visible to any thread that sees the object reference");
        System.out.println("- No synchronization required!");
        System.out.println("- Only applies if 'this' does not escape during construction");
        System.out.println();

        class ImmutablePoint {
            final int x;
            final int y;

            ImmutablePoint(int x, int y) {
                this.x = x; // write to final field
                this.y = y; // write to final field
                // Constructor ends — final fields are now frozen
            }
        }

        ImmutablePoint[] ref = {null};

        Thread publisher = new Thread(() -> {
            ref[0] = new ImmutablePoint(10, 20); // constructor + publication
        });

        Thread reader = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            ImmutablePoint p = ref[0];
            if (p != null) {
                // Without final, p.x and p.y might be 0 (default values).
                // With final, they are guaranteed to be 10 and 20.
                System.out.println("Reader sees: (" + p.x + ", " + p.y + ") (guaranteed (10, 20))");
            }
        });

        publisher.start();
        reader.start();
        publisher.join();
        reader.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 5. volatile vs synchronized — Barrier Comparison
    // ═══════════════════════════════════════════════════════════════

    static void volatileVsSynchronized() throws InterruptedException {
        System.out.println("=== volatile vs synchronized — Barrier Comparison ===");
        System.out.println();

        // volatile: only one variable, no mutual exclusion
        volatile int[] vData = {0};
        volatile boolean[] vFlag = {false};

        // synchronized: can protect multiple variables, provides mutual exclusion
        Object lock = new Object();
        int[] sData = {0};
        boolean[] sFlag = {false};

        // Volatile approach
        Thread vWriter = new Thread(() -> {
            vData[0] = 42;
            vFlag[0] = true; // volatile write
        });
        Thread vReader = new Thread(() -> {
            while (!vFlag[0]) {} // volatile read
            System.out.println("volatile: data=" + vData[0]);
        });

        // Synchronized approach
        Thread sWriter = new Thread(() -> {
            synchronized (lock) {
                sData[0] = 42;
                sFlag[0] = true;
            } // unlock
        });
        Thread sReader = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lock) {
                System.out.println("synchronized: data=" + sData[0]);
            }
        });

        vWriter.start(); vReader.start();
        sWriter.start(); sReader.start();
        vWriter.join(); vReader.join();
        sWriter.join(); sReader.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 6. Out-of-Thin-Air Values
    // ═══════════════════════════════════════════════════════════════
    // The JMM prohibits values that were never written by any thread.

    static void outOfThinAir() {
        System.out.println("=== Out-of-Thin-Air Values ===");
        System.out.println();
        System.out.println("Consider:");
        System.out.println("  int x = 0, y = 0;");
        System.out.println("  // Thread 1:          // Thread 2:");
        System.out.println("  x = 1;                y = 1;");
        System.out.println("  if (y == 0)           if (x == 0)");
        System.out.println("    // x is 1 here        // y is 1 here");
        System.out.println();
        System.out.println("Without the out-of-thin-air rule, an aggressive optimizer could:");
        System.out.println("1. See that if (y==0) then x=1");
        System.out.println("2. See that if (x==0) then y=1");
        System.out.println("3. Conclude: both x and y must be 1");
        System.out.println("4. Set x=1 and y=1 unconditionally");
        System.out.println();
        System.out.println("The JMM explicitly prohibits this 'circular reasoning'.");
        System.out.println("Values must come from actual writes, not from speculation.\n");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║      HAPPENS-BEFORE INTERNALS — DEEP DIVE               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        partialOrdering();
        transitivityChain();
        memoryBarrierTypes();
        finalFieldGuarantee();
        volatileVsSynchronized();
        outOfThinAir();

        System.out.println("Key takeaways:");
        System.out.println("1. Happens-before is a partial order — not all actions are ordered");
        System.out.println("2. Transitivity chains visibility across multiple threads");
        System.out.println("3. Memory barriers are the hardware mechanism behind volatile/synchronized");
        System.out.println("4. final fields provide free safe publication");
        System.out.println("5. The JMM prohibits out-of-thin-air values");
    }
}
