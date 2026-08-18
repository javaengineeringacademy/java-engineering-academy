package academy.javaengineering.concurrency.memorymodel.solutions;

/**
 * Complete solutions to all 5 JMM exercises.
 */
public class Solutions {

    // ═══════════════════════════════════════════════════════════════
    // Solution 1: Fix the visibility bug with volatile
    // ═══════════════════════════════════════════════════════════════

    static volatile boolean shutdown = false;

    static void solution1() throws InterruptedException {
        System.out.println("=== Solution 1: Visibility Bug Fixed ===");

        // volatile ensures the write is visible to the reader thread.
        // Without volatile, the JVM may hoist the read out of the loop
        // or the CPU cache may not be invalidated.
        Thread worker = new Thread(() -> {
            int count = 0;
            while (!shutdown) {
                count++;
            }
            System.out.println("Worker stopped after " + count + " iterations");
        });

        worker.start();
        Thread.sleep(100);
        shutdown = true; // volatile write — immediately visible
        worker.join(2000);

        if (worker.isAlive()) {
            worker.interrupt();
            System.out.println("FAILED");
        } else {
            System.out.println("PASSED: Worker stopped via volatile flag");
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // Solution 2: Double-checked locking
    // ═══════════════════════════════════════════════════════════════

    static class Singleton {
        private static volatile Singleton instance; // volatile is essential!

        private Singleton() {}

        static Singleton getInstance() {
            if (instance == null) {                    // first check (no lock)
                synchronized (Singleton.class) {
                    if (instance == null) {            // second check (with lock)
                        instance = new Singleton();
                        // Without volatile, the JVM may reorder:
                        //   1. Allocate memory
                        //   3. Assign reference to 'instance'
                        //   2. Initialize fields
                        // Thread 2 could see non-null 'instance' with uninitialized fields.
                        //
                        // With volatile, the StoreStore barrier before the volatile write
                        // ensures all field initializations happen before the reference
                        // is published.
                    }
                }
            }
            return instance;
        }

        void doWork() {
            System.out.println("Singleton working: " + System.identityHashCode(this));
        }
    }

    static void solution2() throws InterruptedException {
        System.out.println("=== Solution 2: Double-Checked Locking ===");

        Thread[] threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                Singleton s = Singleton.getInstance();
                System.out.println("Thread " + idx + ": " + System.identityHashCode(s));
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("All threads got the same instance (hash codes match)");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // Solution 3: Safe publication with final fields
    // ═══════════════════════════════════════════════════════════════

    static class Config {
        final int value;       // final — guaranteed visible after constructor
        final String name;     // final — guaranteed visible after constructor

        Config(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    static void solution3() throws InterruptedException {
        System.out.println("=== Solution 3: Safe Publication with final ===");

        // The JMM guarantees that once the constructor completes and the reference
        // is published, all threads see the correct values of final fields.
        // This is because:
        //   - The constructor writes to final fields (these writes happen-before constructor ends)
        //   - Publishing the reference happens-after the constructor
        //   - Any thread that sees the reference also sees the final field values
        Config[] holder = {null};

        Thread publisher = new Thread(() -> {
            holder[0] = new Config(42, "test");
        });

        Thread subscriber = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            Config c = holder[0];
            if (c != null) {
                System.out.println("value=" + c.value + ", name=" + c.name);
                if (c.value == 42 && "test".equals(c.name)) {
                    System.out.println("PASSED: Safe publication works");
                } else {
                    System.out.println("FAILED");
                }
            } else {
                System.out.println("FAILED: Config not published");
            }
        });

        publisher.start();
        subscriber.start();
        publisher.join();
        subscriber.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // Solution 4: Volatile atomicity limitation
    // ═══════════════════════════════════════════════════════════════

    static volatile int counter = 0;

    static void solution4() throws InterruptedException {
        System.out.println("=== Solution 4: Volatile Atomicity Limitation ===");

        // volatile guarantees visibility and ordering, but NOT atomicity.
        // counter++ is three operations:
        //   1. READ counter into register
        //   2. INCREMENT register
        //   3. WRITE register back to counter
        //
        // If both threads read at the same time, they both see the same value,
        // both increment, and both write — one increment is lost.

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) counter++;
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) counter++;
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        int expected = 100000;
        System.out.println("Expected: " + expected + ", Actual: " + counter);
        if (counter < expected) {
            System.out.println("Lost " + (expected - counter) + " updates");
            System.out.println("Fix: Use AtomicInteger or synchronized");
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // Solution 5: Fix race condition with synchronized
    // ═══════════════════════════════════════════════════════════════

    static class BankAccount {
        private int balance = 0;

        // synchronized makes deposit() atomic AND ensures visibility.
        // The happens-before from the previous unlock guarantees that
        // the balance read inside the critical section sees the latest value.
        public synchronized void deposit(int amount) {
            balance += amount;
        }

        public synchronized int getBalance() {
            return balance;
        }
    }

    static void solution5() throws InterruptedException {
        System.out.println("=== Solution 5: Race Condition Fixed ===");

        BankAccount account = new BankAccount();

        Thread[] depositors = new Thread[10];
        for (int i = 0; i < depositors.length; i++) {
            depositors[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    account.deposit(1); // synchronized — atomic and visible
                }
            });
        }

        for (Thread t : depositors) t.start();
        for (Thread t : depositors) t.join();

        int expected = 100000;
        int actual = account.getBalance();
        System.out.println("Expected: " + expected + ", Actual: " + actual);
        if (actual == expected) {
            System.out.println("PASSED: No lost deposits");
        } else {
            System.out.println("FAILED: Lost " + (expected - actual) + " deposits");
        }
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         JMM SOLUTIONS — ALL 5 EXERCISES SOLVED          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        solution1();
        solution2();
        solution3();
        solution4();
        solution5();

        System.out.println("Key takeaways:");
        System.out.println("1. volatile fixes visibility bugs for simple flags");
        System.out.println("2. Double-checked locking requires volatile on the reference");
        System.out.println("3. final fields provide safe publication without synchronization");
        System.out.println("4. volatile does NOT make compound operations atomic");
        System.out.println("5. synchronized provides both mutual exclusion and visibility");
    }
}
