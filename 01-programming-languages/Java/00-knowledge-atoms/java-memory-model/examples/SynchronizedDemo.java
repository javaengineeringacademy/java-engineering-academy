/**
 * Synchronized Demo
 * Demonstrates synchronized blocks and methods for thread safety
 */
public class SynchronizedDemo {

    private int unsafeCount = 0;
    private int safeCount = 0;
    private final Object lock = new Object();

    // Unsafe: not synchronized
    public void unsafeIncrement() {
        unsafeCount++;
    }

    // Safe: synchronized method (locks on 'this')
    public synchronized void safeIncrement() {
        safeCount++;
    }

    // Safe: synchronized block with explicit lock
    public void safeIncrementWithBlock() {
        synchronized (lock) {
            safeCount++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Synchronized Demo ===\n");

        // Demo 1: Unsafe vs Safe increment
        System.out.println("--- Demo 1: Unsafe vs Synchronized ---");
        SynchronizedDemo demo = new SynchronizedDemo();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                demo.unsafeIncrement();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                demo.unsafeIncrement();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Unsafe count:   " + demo.unsafeCount + " (expected 200000)");

        // Reset and test synchronized
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                demo.safeIncrement();
            }
        });
        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                demo.safeIncrement();
            }
        });

        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println("Synchronized count: " + demo.safeCount + " (expected 200000)");
        System.out.println();

        // Demo 2: Synchronized block vs method
        System.out.println("--- Demo 2: Synchronized Block vs Method ---");
        System.out.println("Synchronized method: synchronized void method() { ... }");
        System.out.println("  - Locks on 'this' (the object instance)");
        System.out.println("  - All threads calling this method on same object compete for same lock");
        System.out.println();
        System.out.println("Synchronized block: synchronized (lock) { ... }");
        System.out.println("  - Locks on specified object");
        System.out.println("  - Can use different locks for different resources");
        System.out.println("  - Finer-grained control over lock scope");
        System.out.println();

        // Demo 3: Monitor lock happens-before relationship
        System.out.println("--- Demo 3: Happens-Before with Synchronized ---");
        final boolean[] dataReady = {false};
        final int[] sharedData = {0};

        Thread writer = new Thread(() -> {
            synchronized (demo.lock) {
                sharedData[0] = 42;
                dataReady[0] = true;
                System.out.println("Writer: set sharedData=42, dataReady=true");
            } // unlock happens-before next lock
        });

        Thread reader = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (demo.lock) { // lock happens-after unlock
                System.out.println("Reader: sees sharedData=" + sharedData[0] + ", dataReady=" + dataReady[0]);
                System.out.println("  (guaranteed by monitor lock happens-before rule)");
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();

        // Demo 4: Synchronized on different objects
        System.out.println("--- Demo 4: Lock Granularity ---");
        BankAccount account = new BankAccount(1000);

        Thread withdraw = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                account.withdraw(10);
            }
        });
        Thread deposit = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                account.deposit(10);
            }
        });

        withdraw.start();
        deposit.start();
        withdraw.join();
        deposit.join();

        System.out.println("Final balance: " + account.getBalance() + " (expected 1000)");
        System.out.println("Synchronized methods prevent race conditions");

        System.out.println("\n=== End of Synchronized Demo ===");
    }

    static class BankAccount {
        private int balance;

        public BankAccount(int initialBalance) {
            this.balance = initialBalance;
        }

        // Both methods lock on 'this' - same lock
        public synchronized void withdraw(int amount) {
            if (balance >= amount) {
                balance -= amount;
            }
        }

        public synchronized void deposit(int amount) {
            balance += amount;
        }

        public synchronized int getBalance() {
            return balance;
        }
    }
}

/*
Expected Output (approximate):
=== Synchronized Demo ===

--- Demo 1: Unsafe vs Synchronized ---
Unsafe count:   187432 (varies, not 200000)
Synchronized count: 200000 (expected 200000)

--- Demo 2: Synchronized Block vs Method ---
Synchronized method: synchronized void method() { ... }
  - Locks on 'this' (the object instance)
  - All threads calling this method on same object compete for same lock

Synchronized block: synchronized (lock) { ... }
  - Locks on specified object
  - Can use different locks for different resources
  - Finer-grained control over lock scope

--- Demo 3: Happens-Before with Synchronized ---
Writer: set sharedData=42, dataReady=true
Reader: sees sharedData=42, dataReady=true
  (guaranteed by monitor lock happens-before rule)

--- Demo 4: Lock Granularity ---
Final balance: 1000 (expected 1000)
Synchronized methods prevent race conditions

=== End of Synchronized Demo ===
*/
