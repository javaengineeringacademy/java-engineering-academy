/**
 * Volatile Demo
 * Demonstrates volatile keyword for visibility and ordering guarantees
 */
public class VolatileDemo {

    // volatile guarantees visibility across threads
    private volatile boolean running = true;
    private volatile int sharedValue = 0;
    private volatile boolean ready = false;
    private int nonVolatileValue = 0;

    public void startWorker() {
        Thread worker = new Thread(() -> {
            System.out.println("Worker started, waiting for ready signal...");
            while (!ready) {
                // Wait for ready signal
            }
            System.out.println("Worker sees ready=true, sharedValue=" + sharedValue);
            System.out.println("Worker sees nonVolatileValue=" + nonVolatileValue);
        });
        worker.start();
    }

    public void startShutdownWorker() {
        Thread worker = new Thread(() -> {
            System.out.println("Shutdown worker started, running=" + running);
            while (running) {
                // Loop until running becomes false
            }
            System.out.println("Shutdown worker stopped (saw running=false)");
        });
        worker.start();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Volatile Demo ===\n");

        // Demo 1: Volatile for visibility
        System.out.println("--- Demo 1: Volatile Visibility ---");
        VolatileDemo demo1 = new VolatileDemo();
        demo1.startWorker();

        Thread.sleep(100);
        demo1.nonVolatileValue = 42;
        demo1.sharedValue = 100;
        demo1.ready = true; // volatile write - visible immediately

        Thread.sleep(100);
        System.out.println();

        // Demo 2: Volatile for shutdown flag
        System.out.println("--- Demo 2: Volatile Shutdown Flag ---");
        VolatileDemo demo2 = new VolatileDemo();
        demo2.startShutdownWorker();

        Thread.sleep(100);
        demo2.running = false; // volatile write - guaranteed visible
        Thread.sleep(100);
        System.out.println();

        // Demo 3: Volatile ordering guarantee
        System.out.println("--- Demo 3: Volatile Ordering (Memory Barrier) ---");
        VolatileDemo demo3 = new VolatileDemo();
        Thread reader = new Thread(() -> {
            while (!demo3.ready) {
                // Wait
            }
            // After volatile read of ready, all prior writes are visible
            System.out.println("Reader sees: a=" + demo3.a + ", b=" + demo3.b + ", ready=" + demo3.ready);
        });

        demo3.a = 1;   // non-volatile write
        demo3.b = 2;   // non-volatile write
        reader.start();
        Thread.sleep(50);
        demo3.ready = true; // volatile write - orders a and b before ready

        Thread.sleep(100);
        System.out.println();

        // Demo 4: Volatile does NOT provide atomicity
        System.out.println("--- Demo 4: Volatile is NOT Atomic ---");
        VolatileCounter counter = new VolatileCounter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Expected: 200000");
        System.out.println("Actual:   " + counter.getCount());
        System.out.println("volatile int count++ is NOT atomic (read-modify-write)");
        System.out.println("Use AtomicInteger for atomic operations");

        // Demo 5: Volatile vs synchronized
        System.out.println("\n--- Demo 5: When to Use Volatile vs Synchronized ---");
        System.out.println("volatile:");
        System.out.println("  - State flags (running, ready)");
        System.out.println("  - Immutable objects (safe publication)");
        System.out.println("  - One write, many reads");
        System.out.println("\nsynchronized:");
        System.out.println("  - Compound operations (check-then-act)");
        System.out.println("  - Multiple related writes");
        System.out.println("  - When you need both visibility and atomicity");

        System.out.println("\n=== End of Volatile Demo ===");
    }

    int a = 0;
    int b = 0;
    volatile boolean ready = false;

    static class VolatileCounter {
        private volatile int count = 0; // volatile, but increment is NOT atomic

        public void increment() {
            count++; // read count, add 1, write count - not atomic!
        }

        public int getCount() {
            return count;
        }
    }
}

/*
Expected Output (approximate):
=== Volatile Demo ===

--- Demo 1: Volatile Visibility ---
Worker started, waiting for ready signal...
Worker sees ready=true, sharedValue=100
Worker sees nonVolatileValue=42

--- Demo 2: Volatile Shutdown Flag ---
Shutdown worker started, running=true
Shutdown worker stopped (saw running=false)

--- Demo 3: Volatile Ordering (Memory Barrier) ---
Reader sees: a=1, b=2, ready=true

--- Demo 4: Volatile is NOT Atomic ---
Expected: 200000
Actual:   187432 (varies, not 200000)
volatile int count++ is NOT atomic (read-modify-write)
Use AtomicInteger for atomic operations

--- Demo 5: When to Use Volatile vs Synchronized ---
volatile:
  - State flags (running, ready)
  - Immutable objects (safe publication)
  - One write, many reads

synchronized:
  - Compound operations (check-then-act)
  - Multiple related writes
  - When you need both visibility and atomicity

=== End of Volatile Demo ===
*/
