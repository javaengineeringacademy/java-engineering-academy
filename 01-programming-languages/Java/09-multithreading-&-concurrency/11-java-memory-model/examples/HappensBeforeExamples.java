package academy.javaengineering.concurrency.memorymodel.examples;

/**
 * Demonstrates every happens-before rule defined by the JMM.
 * Each rule guarantees that writes by one thread are visible to reads by another.
 */
public class HappensBeforeExamples {

    // --- Rule 1: Program Order Rule ---
    // Within a single thread, each action happens-before the next in program order.
    static void programOrderRule() throws InterruptedException {
        System.out.println("=== Program Order Rule ===");
        System.out.println("Within a thread, actions execute in program order.");

        int[] result = new int[1];
        Thread t = new Thread(() -> {
            // These writes happen-before each other by program order
            int a = 1;
            int b = 2;
            int c = a + b;
            result[0] = c; // guaranteed to be 3
        });
        t.start();
        t.join();
        System.out.println("Result: " + result[0] + " (guaranteed 3)");
        System.out.println("This is trivial alone — it matters when combined with other rules.\n");
    }

    // --- Rule 2: Monitor Lock Rule (unlock → lock) ---
    // An unlock on a monitor happens-before every subsequent lock on the same monitor.
    static void monitorLockRule() throws InterruptedException {
        System.out.println("=== Monitor Lock Rule ===");
        System.out.println("An unlock of a monitor happens-before every subsequent lock on the same monitor.");

        Object lock = new Object();
        int[] shared = new int[1];

        // Thread 1 writes
        Thread writer = new Thread(() -> {
            synchronized (lock) {
                shared[0] = 42;
            } // UNLOCK — all writes before this are flushed
        });

        // Thread 2 reads (may run much later)
        Thread reader = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lock) { // LOCK — sees all writes before the unlock
                System.out.println("Reader sees: " + shared[0] + " (guaranteed 42)");
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("The lock on the same monitor establishes happens-before.\n");
    }

    // --- Rule 3: Volatile Variable Rule (write → read) ---
    // A write to a volatile variable happens-before every subsequent read of the same variable.
    static void volatileRule() throws InterruptedException {
        System.out.println("=== Volatile Variable Rule ===");
        System.out.println("A volatile write happens-before every subsequent volatile read of the same field.");

        volatile boolean ready = false;
        int[] data = new int[1];

        Thread writer = new Thread(() -> {
            data[0] = 42;
            ready = true; // volatile WRITE
        });

        Thread reader = new Thread(() -> {
            while (!ready) { /* spin */ }
            // ready volatile READ sees true
            // Because volatile WRITE HB volatile READ:
            // data[0] = 42 HB ready = true HB !ready exits loop
            // Therefore data[0] == 42 is guaranteed
            System.out.println("Reader sees data: " + data[0] + " (guaranteed 42)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Volatile write-read pair establishes happens-before.\n");
    }

    // --- Rule 4: Thread Start Rule ---
    // Thread.start() happens-before every action in the started thread.
    static void threadStartRule() throws InterruptedException {
        System.out.println("=== Thread Start Rule ===");
        System.out.println("Thread.start() happens-before every action in the started thread.");

        int[] shared = new int[1];
        shared[0] = 100;

        Thread t = new Thread(() -> {
            // This action happens-after start() returns
            System.out.println("Started thread sees: " + shared[0] + " (guaranteed 100)");
        });

        shared[0] = 100; // write BEFORE start()
        t.start();        // start() happens-before run() body
        t.join();
        System.out.println("All writes before start() are visible to the started thread.\n");
    }

    // --- Rule 5: Thread Termination Rule (all actions → join() returns) ---
    // All actions in a thread happen-before another thread's join() returns.
    static void threadTerminationRule() throws InterruptedException {
        System.out.println("=== Thread Termination Rule ===");
        System.out.println("All actions in a thread happen-before join() returns in another thread.");

        int[] result = new int[1];

        Thread worker = new Thread(() -> {
            result[0] = 200; // action in worker thread
            System.out.println("Worker computed: " + result[0]);
        });

        worker.start();
        worker.join(); // join() returns happens-after all actions in worker

        // result[0] is guaranteed to be 200
        System.out.println("After join(), reader sees: " + result[0] + " (guaranteed 200)");
        System.out.println("join() establishes happens-before from all worker actions.\n");
    }

    // --- Rule 6: Transitivity ---
    // If A HB B and B HB C, then A HB C.
    static void transitivityRule() throws InterruptedException {
        System.out.println("=== Transitivity Rule ===");
        System.out.println("If A happens-before B and B happens-before C, then A happens-before C.");

        int[] data = new int[1];
        volatile boolean flag = false;

        // Thread 1: writes data, then flag
        Thread writer = new Thread(() -> {
            data[0] = 999;       // Action A
            flag = true;          // Action B (volatile write)
            // A HB B by program order (Rule 1)
        });

        // Thread 2: reads flag, then data
        Thread reader = new Thread(() -> {
            while (!flag) {}      // Action C (volatile read)
            // B HB C by volatile rule (Rule 3)
            // By transitivity: A HB C → data[0] = 999 HB the read of flag
            System.out.println("Reader sees data: " + data[0] + " (guaranteed 999)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Chain: write data HB write flag HB read flag → data visible!\n");
    }

    // --- Rule 7: Interruption Rule ---
    // An interrupt on a thread happens-before any thread detects the interrupt.
    static void interruptionRule() throws InterruptedException {
        System.out.println("=== Interruption Rule ===");
        System.out.println("An interrupt happens-before the interrupted thread detects it.");

        final boolean[] detected = {false};

        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(10000); // will be interrupted
            } catch (InterruptedException e) {
                detected[0] = true;
                // The interrupt() call HB this catch block
                System.out.println("Interrupt detected in thread");
            }
        });

        sleeper.start();
        Thread.sleep(50);
        sleeper.interrupt(); // interrupt() HB the catch block
        sleeper.join();
        System.out.println("Interruption detected: " + detected[0]);
        System.out.println("The interrupt call happens-before the thread's detection.\n");
    }

    // --- Rule 8: Finalizer Rule ---
    // The end of a constructor happens-before the start of the finalizer.
    static void finalizerRule() {
        System.out.println("=== Finalizer Rule ===");
        System.out.println("Constructor completion happens-before finalizer execution.");

        class Tracked {
            final int value;
            Tracked(int v) { this.value = v; }
            @Override
            protected void finalize() {
                // Guaranteed to see value == assigned value
                // because constructor HB finalize
                System.out.println("Finalizer sees value: " + value);
            }
        }

        Tracked t = new Tracked(777);
        t = null; // eligible for GC
        System.gc(); // suggest GC (not guaranteed, but helps)
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        System.out.println("Finalizer sees correct value because constructor HB finalize.\n");
    }

    // --- Rule 9: Object Constructor Rule ---
    // Writing to a field in a constructor happens-before the first action in a subthread
    // that starts during the constructor.
    static void objectConstructorRule() throws InterruptedException {
        System.out.println("=== Object Constructor Rule ===");
        System.out.println("Writing 'this.x = value' in a constructor HB actions in subthreads started during construction.");

        class EventSource {
            final int data;
            final Thread listener;

            EventSource() {
                data = 100; // field write in constructor
                // Start a subthread that will read this.data
                listener = new Thread(() -> {
                    // This action happens-after the field write
                    // because of the Object Constructor Rule
                    System.out.println("Listener sees data: " + data + " (guaranteed 100)");
                });
                listener.start();
            }
        }

        EventSource source = new EventSource();
        source.listener.join();
        System.out.println("Field assignment in constructor is visible to subthread started during construction.\n");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       HAPPENS-BEFORE RULES — COMPLETE DEMONSTRATION     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        programOrderRule();
        monitorLockRule();
        volatileRule();
        threadStartRule();
        threadTerminationRule();
        transitivityRule();
        interruptionRule();
        finalizerRule();
        objectConstructorRule();

        System.out.println("All 9 happens-before rules demonstrated.");
        System.out.println("Each rule guarantees memory visibility between threads.");
    }
}
