package academy.javaengineering.concurrency.memorymodel.examples;

/**
 * Comprehensive examples of the Java Memory Model in practice.
 * Covers safe publication, DCL, immutable objects, and real-world patterns.
 */
public class MemoryModelExamples {

    // ═══════════════════════════════════════════════════════════════
    // 1. Double-Checked Locking (Correct with volatile)
    // ═══════════════════════════════════════════════════════════════

    static class Singleton {
        private static volatile Singleton instance; // volatile is essential
        private final int value;

        private Singleton() {
            this.value = 42;
        }

        static Singleton getInstance() {
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        instance = new Singleton();
                        // volatile write prevents reordering of:
                        //   1. allocate memory → 2. assign reference → 3. initialize fields
                        // Without volatile, step 2 could happen before step 3.
                    }
                }
            }
            return instance;
        }
    }

    static void doubleCheckedLocking() throws InterruptedException {
        System.out.println("=== Double-Checked Locking ===");

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                Singleton s = Singleton.getInstance();
                System.out.println("Thread " + idx + ": value=" + s.value + ", hash=" + System.identityHashCode(s));
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 2. Safe Publication with final Fields
    // ═══════════════════════════════════════════════════════════════

    static class SafePublication {
        final int x;
        final String name;

        SafePublication(int x, String name) {
            this.x = x;
            this.name = name;
        }
    }

    static void safePublication() throws InterruptedException {
        System.out.println("=== Safe Publication with final Fields ===");

        SafePublication[] ref = {null};

        Thread publisher = new Thread(() -> {
            ref[0] = new SafePublication(42, "config");
        });

        Thread subscriber = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            SafePublication p = ref[0];
            if (p != null) {
                // final fields guaranteed visible
                System.out.println("Published: x=" + p.x + ", name=" + p.name);
            }
        });

        publisher.start();
        subscriber.start();
        publisher.join();
        subscriber.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 3. Unsafe Publication (Without final/volatile)
    // ═══════════════════════════════════════════════════════════════

    static class UnsafePublication {
        int x;
        String name;

        UnsafePublication(int x, String name) {
            this.x = x;
            this.name = name;
        }
    }

    static void unsafePublication() throws InterruptedException {
        System.out.println("=== Unsafe Publication ===");
        System.out.println("Without final or volatile, fields may not be visible.");

        UnsafePublication[] ref = {null};

        Thread publisher = new Thread(() -> {
            ref[0] = new UnsafePublication(42, "config");
        });

        Thread subscriber = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            UnsafePublication p = ref[0];
            if (p != null) {
                // NOT guaranteed to see 42 and "config"
                System.out.println("Read: x=" + p.x + ", name=" + p.name + " (may be 0, null)");
            }
        });

        publisher.start();
        subscriber.start();
        publisher.join();
        subscriber.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 4. Immutable Object Pattern
    // ═══════════════════════════════════════════════════════════════

    static class ImmutableVector {
        final double x;
        final double y;
        final double z;

        ImmutableVector(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        ImmutableVector add(ImmutableVector other) {
            return new ImmutableVector(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
            );
        }

        double magnitude() {
            return Math.sqrt(x * x + y * y + z * z);
        }

        @Override
        public String toString() {
            return String.format("(%.1f, %.1f, %.1f)", x, y, z);
        }
    }

    static void immutableObject() throws InterruptedException {
        System.out.println("=== Immutable Object Pattern ===");
        System.out.println("Immutable objects with final fields are inherently thread-safe.");

        ImmutableVector[] shared = {new ImmutableVector(1, 0, 0)};

        Thread writer = new Thread(() -> {
            shared[0] = new ImmutableVector(3, 4, 0);
        });

        Thread reader = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            ImmutableVector v = shared[0];
            // v is either (1,0,0) or (3,4,0) — never a partially constructed object
            System.out.println("Vector: " + v + ", magnitude=" + String.format("%.1f", v.magnitude()));
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 5. The "This Escape" Problem
    // ═══════════════════════════════════════════════════════════════

    static class EventListener {
        void onEvent(String event) {
            System.out.println("Event: " + event);
        }
    }

    static class EventSource {
        final int id;

        EventSource(EventListener listener) {
            this.id = 42;
            // PROBLEM: passing 'this' to a callback during construction
            // The listener may see id == 0 (default) instead of 42
            // because the constructor hasn't finished yet.
            registerListener(listener);
        }

        void registerListener(EventListener listener) {
            // In a real app, this might store the listener for later use
        }
    }

    static void thisEscape() {
        System.out.println("=== The 'this' Escape Problem ===");
        System.out.println("Passing 'this' during construction can expose partially constructed objects.");
        System.out.println("Solution: Don't pass 'this' in the constructor. Use a factory method.");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 6. volatile for Cancel Flag
    // ═══════════════════════════════════════════════════════════════

    static void volatileCancelFlag() throws InterruptedException {
        System.out.println("=== volatile Cancel Flag Pattern ===");

        volatile boolean[] cancelled = {false};

        Thread worker = new Thread(() -> {
            int iterations = 0;
            while (!cancelled[0]) {
                iterations++;
                // do work
            }
            System.out.println("Worker stopped after " + iterations + " iterations");
        });

        worker.start();
        Thread.sleep(50);
        cancelled[0] = true; // volatile write — immediately visible
        worker.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 7. volatile for Lazy Initialization
    // ═══════════════════════════════════════════════════════════════

    static class LazyResource {
        volatile int[] data = null;

        int[] getData() {
            if (data == null) {
                synchronized (this) {
                    if (data == null) {
                        data = new int[]{1, 2, 3, 4, 5};
                        System.out.println("Initialized data");
                    }
                }
            }
            return data;
        }
    }

    static void lazyInit() throws InterruptedException {
        System.out.println("=== volatile Lazy Initialization ===");

        LazyResource resource = new LazyResource();

        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                int[] d = resource.getData();
                System.out.println("Thread " + idx + ": " + java.util.Arrays.toString(d));
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 8. Happens-Before Chain in Practice
    // ═══════════════════════════════════════════════════════════════

    static void happensBeforeChain() throws InterruptedException {
        System.out.println("=== Happens-Before Chain in Practice ===");
        System.out.println("Combining volatile and program order for multi-variable visibility.");

        volatile boolean[] dataReady = {false};
        int[] payload = {0};
        volatile boolean[] processed = {false};
        int[] result = {0};

        // Stage 1: Data producer
        Thread producer = new Thread(() -> {
            payload[0] = 100;         // non-volatile write
            dataReady[0] = true;      // volatile write
        });

        // Stage 2: Processor
        Thread processor = new Thread(() -> {
            while (!dataReady[0]) {}  // volatile read
            result[0] = payload[0] * 2; // guaranteed to see 100
            processed[0] = true;      // volatile write
        });

        // Stage 3: Consumer
        Thread consumer = new Thread(() -> {
            while (!processed[0]) {}  // volatile read
            System.out.println("Final result: " + result[0] + " (guaranteed 200)");
        });

        producer.start();
        processor.start();
        consumer.start();
        producer.join();
        processor.join();
        consumer.join();
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║    MEMORY MODEL EXAMPLES — COMPREHENSIVE GUIDE          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        doubleCheckedLocking();
        safePublication();
        unsafePublication();
        immutableObject();
        thisEscape();
        volatileCancelFlag();
        lazyInit();
        happensBeforeChain();

        System.out.println("Key patterns:");
        System.out.println("1. DCL requires volatile on the reference");
        System.out.println("2. final fields provide free safe publication");
        System.out.println("3. Immutable objects are inherently thread-safe");
        System.out.println("4. Don't pass 'this' during construction");
        System.out.println("5. volatile is perfect for cancel/status flags");
        System.out.println("6. volatile + DCL for lazy initialization");
    }
}
