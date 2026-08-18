package academy.javaengineering.concurrency.memorymodel.practices;

/**
 * 5 hands-on exercises for the Java Memory Model.
 * Each exercise demonstrates a real JMM concept.
 * Complete the TODO sections.
 */
public class Practices {

    // ═══════════════════════════════════════════════════════════════
    // Exercise 1: Fix the visibility bug
    // ═══════════════════════════════════════════════════════════════
    // The worker thread should stop when shutdown is requested.
    // Currently, it may spin forever. Fix it using volatile.

    static volatile boolean[] shutdown = {false};

    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Fix the Visibility Bug ===");

        // TODO: The worker thread may never see shutdown[0] = true.
        // Fix this so the worker stops within 200ms of the flag being set.
        // Hint: What keyword makes a write visible to other threads?

        Thread worker = new Thread(() -> {
            int count = 0;
            while (!shutdown[0]) {
                count++;
                if (count > 100_000_000) break; // safety valve
            }
            System.out.println("Worker stopped after " + count + " iterations");
        });

        worker.start();
        Thread.sleep(100);
        shutdown[0] = true;
        worker.join(2000);

        if (worker.isAlive()) {
            worker.interrupt();
            System.out.println("FAILED: Worker did not stop in time");
        } else {
            System.out.println("PASSED: Worker stopped gracefully");
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // Exercise 2: Implement double-checked locking correctly
    // ═══════════════════════════════════════════════════════════════
    // Implement a thread-safe lazy singleton using double-checked locking.
    // The instance field MUST be volatile to prevent reordering.

    static class Singleton {
        // TODO: Add the volatile keyword to prevent reordering
        private static Singleton instance;

        private Singleton() {}

        public static Singleton getInstance() {
            // TODO: Implement double-checked locking
            // 1. Check if instance is null (no lock)
            // 2. If null, synchronized on Singleton.class
            // 3. Check again inside the synchronized block
            // 4. If still null, create new Singleton()
            throw new UnsupportedOperationException("TODO: implement getInstance()");
        }

        public void doWork() {
            System.out.println("Singleton working: " + System.identityHashCode(this));
        }
    }

    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Double-Checked Locking ===");

        // TODO: Uncomment when getInstance() is implemented
        // Thread[] threads = new Thread[20];
        // for (int i = 0; i < threads.length; i++) {
        //     final int idx = i;
        //     threads[i] = new Thread(() -> {
        //         Singleton s = Singleton.getInstance();
        //         System.out.println("Thread " + idx + ": " + System.identityHashCode(s));
        //     });
        //     threads[i].start();
        // }
        // for (Thread t : threads) t.join();
        System.out.println("TODO: Implement Singleton.getInstance()");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // Exercise 3: Show how final fields guarantee safe publication
    // ═══════════════════════════════════════════════════════════════
    // Create an immutable object with final fields and demonstrate
    // that it can be safely published without synchronization.

    // TODO: Create an immutable Config class with:
    // - A final int value
    // - A final String name
    // - A constructor that initializes both fields

    static class Config {
        // TODO: Add final fields and constructor
        int value; // make this final
        String name; // make this final

        Config(int value, String name) {
            // TODO: initialize fields
            this.value = value;
            this.name = name;
        }
    }

    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Safe Publication with final ===");

        // TODO: Make Config fields final, then uncomment the test
        Config[] holder = {null};

        Thread publisher = new Thread(() -> {
            holder[0] = new Config(42, "test");
        });

        Thread subscriber = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            Config c = holder[0];
            if (c != null) {
                // With final fields, these are guaranteed to be 42 and "test"
                System.out.println("value=" + c.value + ", name=" + c.name);
                if (c.value == 42 && "test".equals(c.name)) {
                    System.out.println("PASSED: Safe publication works");
                } else {
                    System.out.println("FAILED: Fields not properly visible");
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
    // Exercise 4: Demonstrate volatile atomicity limitation
    // ═══════════════════════════════════════════════════════════════
    // Show that volatile does NOT guarantee atomicity for compound
    // operations like count++.

    static volatile int[] counter = {0};

    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Volatile Atomicity Limitation ===");

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) counter[0]++;
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) counter[0]++;
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        int expected = 100000;
        System.out.println("Expected: " + expected + ", Actual: " + counter[0]);
        if (counter[0] < expected) {
            System.out.println("PASSED: Lost updates demonstrate volatile atomicity limitation");
            System.out.println("Lost " + (expected - counter[0]) + " updates");
        } else {
            System.out.println("NOTE: No lost updates this time (race conditions are non-deterministic)");
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // Exercise 5: Fix a race condition with synchronized
    // ═══════════════════════════════════════════════════════════════
    // The BankAccount class has a race condition. Fix it so that
    // concurrent deposits don't lose updates.

    static class BankAccount {
        private int balance = 0;

        // TODO: Fix this method so concurrent calls don't lose updates
        public void deposit(int amount) {
            balance += amount; // race condition!
        }

        public int getBalance() {
            return balance;
        }
    }

    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Fix Race Condition with synchronized ===");

        BankAccount account = new BankAccount();

        Thread[] depositors = new Thread[10];
        for (int i = 0; i < depositors.length; i++) {
            depositors[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    account.deposit(1); // TODO: make this thread-safe
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
        System.out.println("║         JMM PRACTICES — 5 EXERCISES                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();

        System.out.println("Complete all exercises in Solutions.java to verify your answers.");
    }
}
