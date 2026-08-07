import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solution 1: Visibility Fix
 * Complete implementation with all fixes
 */
public class VisibilitySolution {

    // FIXED: Using volatile for simple flag
    static class FixedVisibility {
        private volatile boolean running = true;

        public void start() {
            new Thread(() -> {
                while (running) {
                    // volatile read - always sees latest value
                }
                System.out.println("Worker stopped");
            }).start();
        }

        public void stop() {
            running = false;
        }
    }

    // SOLUTION 2: Thread-Safe Counter Implementations
    static class SynchronizedCounter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized int getCount() {
            return count;
        }
    }

    static class VolatileCounter {
        // volatile does NOT make count++ atomic
        // count++ = read + add + write (3 operations)
        // Another thread can interleave between read and write
        private volatile int count = 0;

        public void increment() {
            count++; // NOT thread-safe
        }

        public int getCount() {
            return count;
        }
    }

    static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            count.incrementAndGet(); // atomic CAS operation
        }

        public int getCount() {
            return count.get();
        }
    }

    // SOLUTION 3: Happens-Before Chain Demo
    static class HappensBeforeChain {
        private int data1 = 0;
        private int data2 = 0;
        private volatile boolean ready = false;

        public void produce() {
            data1 = 10;       // action 1
            data2 = 20;       // action 2
            ready = true;     // volatile write (action 3)
            // 1,2 happen-before 3 (program order)
        }

        public void consume() throws InterruptedException {
            while (!ready) {
                Thread.sleep(10);
            }
            // 3 happens-before 4 (volatile rule)
            // 4 happens-before 5 (program order)
            // By transitivity: 1,2 happen-before 5
            System.out.println("  Consumer sees: data1=" + data1 + ", data2=" + data2);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Java Memory Model Solutions ===\n");

        // Solution 1: Fixed Visibility
        System.out.println("--- Solution 1: Visibility Fix ---");
        FixedVisibility fixed = new FixedVisibility();
        fixed.start();
        Thread.sleep(100);
        fixed.stop();
        Thread.sleep(100);
        System.out.println("Worker correctly stopped using volatile\n");

        // Solution 2: Counter Comparison
        System.out.println("--- Solution 2: Thread-Safe Counter ---");
        testCounter("Synchronized", new SynchronizedCounter());
        testCounter("Volatile (broken)", new VolatileCounter());
        testCounter("Atomic", new AtomicCounter());

        System.out.println();
        System.out.println("Key Takeaways:");
        System.out.println("  - synchronized: ensures both visibility and atomicity");
        System.out.println("  - volatile: ensures visibility only, not atomicity");
        System.out.println("  - AtomicInteger: lock-free atomic operations");

        // Solution 3: Happens-Before Chain
        System.out.println("\n--- Solution 3: Happens-Before Chain ---");
        HappensBeforeChain chain = new HappensBeforeChain();

        Thread producer = new Thread(() -> chain.produce());
        Thread consumer = new Thread(() -> {
            try { chain.consume(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("  Chain: program order -> volatile rule -> transitivity");

        System.out.println("\n=== End of Solutions ===");
    }

    interface Counter {
        void increment();
        int getCount();
    }

    private static void testCounter(String name, Object counterObj) throws InterruptedException {
        Counter counter;
        if (counterObj instanceof SynchronizedCounter) {
            SynchronizedCounter sc = (SynchronizedCounter) counterObj;
            counter = new Counter() {
                public void increment() { sc.increment(); }
                public int getCount() { return sc.getCount(); }
            };
        } else if (counterObj instanceof VolatileCounter) {
            VolatileCounter vc = (VolatileCounter) counterObj;
            counter = new Counter() {
                public void increment() { vc.increment(); }
                public int getCount() { return vc.getCount(); }
            };
        } else {
            AtomicCounter ac = (AtomicCounter) counterObj;
            counter = new Counter() {
                public void increment() { ac.increment(); }
                public int getCount() { return ac.getCount(); }
            };
        }

        Thread t1 = new Thread(() -> { for (int i = 0; i < 100000; i++) counter.increment(); });
        Thread t2 = new Thread(() -> { for (int i = 0; i < 100000; i++) counter.increment(); });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.printf("%-25s Expected: 200000, Actual: %d%n", name, counter.getCount());
    }
}
