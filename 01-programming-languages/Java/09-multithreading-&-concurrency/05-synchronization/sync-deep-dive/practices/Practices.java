package academy.javaengineering.concurrency.sync.deeprdive.practices;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.concurrent.TimeUnit;

/**
 * Synchronization Deep Dive — 5 Practice Exercises.
 *
 * Each exercise tests understanding of synchronization concepts:
 *   1. Thread-safe bank account with synchronized methods
 *   2. Deadlock creation and prevention with tryLock
 *   3. ReadWriteLock for concurrent readers + exclusive writer
 *   4. Lock ordering to prevent deadlock across multiple resources
 *   5. Reentrancy demonstration — synchronized method calling itself
 *
 * Implement each method. Run the main() method to verify your solutions.
 * See solutions/Solutions.java for complete implementations.
 */
public class Practices {

    // ─── Exercise 1: Thread-Safe Bank Account ──────────────────────────
    /**
     * Create a thread-safe BankAccount class.
     *
     * Requirements:
     * - Fields: balance (double), accountNumber (String)
     * - Constructor initializes balance and accountNumber
     * - deposit(double amount): synchronized, adds amount to balance
     * - withdraw(double amount): synchronized, subtracts amount if sufficient funds
     * - getBalance(): synchronized, returns current balance
     * - transferTo(BankAccount other, double amount): synchronized on THIS account,
     *   then synchronized on OTHER account (use lock ordering by accountNumber hashCode)
     *
     * The transferTo method must:
     *   1. Determine lock order based on accountNumber hashCode
     *   2. Acquire locks in consistent order (lower hashCode first)
     *   3. Withdraw from this account
     *   4. Deposit to other account
     *   5. Release locks in reverse order
     */
    static class BankAccount {
        // TODO: Implement this class
        // private double balance;
        // private final String accountNumber;

        public BankAccount(String accountNumber, double initialBalance) {
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized void deposit(double amount) {
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized boolean withdraw(double amount) {
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized double getBalance() {
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized void transferTo(BankAccount other, double amount) {
            throw new UnsupportedOperationException("Implement this");
        }

        public String getAccountNumber() {
            throw new UnsupportedOperationException("Implement this");
        }
    }

    // ─── Exercise 2: Deadlock Prevention with tryLock ──────────────────
    /**
     * Implement a deadlock-free resource acquisition pattern.
     *
     * Requirements:
     * - Two ReentrantLock resources: resourceA, resourceB
     * - acquireBoth(long timeoutMs): tries to acquire both locks using tryLock with timeout
     *   - If both acquired: returns true
     *   - If timeout: releases any acquired lock, returns false
     * - releaseBoth(): releases both locks (call in finally block)
     * - Work(): acquires both locks, prints "Working...", sleeps 10ms, releases both
     */
    static class ResourceAcquisition {
        private final ReentrantLock resourceA = new ReentrantLock();
        private final ReentrantLock resourceB = new ReentrantLock();

        public boolean acquireBoth(long timeoutMs) {
            // TODO: Implement tryLock with timeout for both resources
            throw new UnsupportedOperationException("Implement this");
        }

        public void releaseBoth() {
            // TODO: Release both locks (check isHeldByCurrentThread before unlock)
            throw new UnsupportedOperationException("Implement this");
        }

        public void work() {
            // TODO: Use acquireBoth and releaseBoth to safely work with both resources
            throw new UnsupportedOperationException("Implement this");
        }
    }

    // ─── Exercise 3: ReadWriteLock Counter ─────────────────────────────
    /**
     * Implement a counter using ReadWriteLock.
     *
     * Requirements:
     * - Uses ReentrantReadWriteLock
     * - increment(): acquires write lock, increments count, releases
     * - readCount(): acquires read lock, reads count, releases
     * - Multiple threads can read simultaneously
     * - Only one thread can write at a time
     * - Prints thread names to show concurrent reads vs exclusive writes
     */
    static class ReadWriteCounter {
        // TODO: Implement with ReadWriteLock
        // private int count = 0;
        // private final ReadWriteLock rwLock = ...;

        public void increment() {
            throw new UnsupportedOperationException("Implement this");
        }

        public int readCount() {
            throw new UnsupportedOperationException("Implement this");
        }
    }

    // ─── Exercise 4: Multi-Resource Lock Ordering ──────────────────────
    /**
     * Implement safe acquisition of 4 resources using lock ordering.
     *
     * Requirements:
     * - Four ReentrantLock resources: res1, res2, res3, res4
     * - acquireInOrder(): acquires all 4 locks in order (res1 → res2 → res3 → res4)
     * - releaseInOrder(): releases all 4 locks in reverse order (res4 → res3 → res2 → res1)
     * - safeWork(): acquires all, prints "Working with all 4 resources", sleeps 5ms, releases all
     * - Must be safe against deadlock even with multiple threads
     */
    static class MultiResourceLock {
        private final ReentrantLock res1 = new ReentrantLock();
        private final ReentrantLock res2 = new ReentrantLock();
        private final ReentrantLock res3 = new ReentrantLock();
        private final ReentrantLock res4 = new ReentrantLock();

        public void acquireInOrder() {
            // TODO: Lock res1 → res2 → res3 → res4
            throw new UnsupportedOperationException("Implement this");
        }

        public void releaseInOrder() {
            // TODO: Unlock res4 → res3 → res2 → res1
            throw new UnsupportedOperationException("Implement this");
        }

        public void safeWork() {
            // TODO: acquireInOrder, print, sleep, releaseInOrder (in finally)
            throw new UnsupportedOperationException("Implement this");
        }
    }

    // ─── Exercise 5: Reentrancy Chain ──────────────────────────────────
    /**
     * Demonstrate reentrancy with a chain of synchronized calls.
     *
     * Requirements:
     * - Shared counter field (int count = 0)
     * - methodA(): synchronized, increments count by 1, calls methodB()
     * - methodB(): synchronized, increments count by 10, calls methodC()
     * - methodC(): synchronized, increments count by 100, calls methodD()
     * - methodD(): synchronized, increments count by 1000
     * - All methods sync on same `this` — demonstrate reentrancy
     * - getCount(): returns count
     *
     * After calling methodA(), count should be 1111 (1 + 10 + 100 + 1000)
     */
    static class ReentrantChain {
        private int count = 0;

        public synchronized void methodA() {
            // TODO: increment by 1, call methodB
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized void methodB() {
            // TODO: increment by 10, call methodC
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized void methodC() {
            // TODO: increment by 100, call methodD
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized void methodD() {
            // TODO: increment by 1000
            throw new UnsupportedOperationException("Implement this");
        }

        public synchronized int getCount() {
            return count;
        }
    }

    // ─── Main — Test Runner ────────────────────────────────────────────
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("  Synchronization Deep Dive — Exercises");
        System.out.println("========================================\n");

        // Test Exercise 1
        System.out.println("--- Exercise 1: Thread-Safe Bank Account ---");
        try {
            BankAccount acc1 = new BankAccount("ACC-001", 1000);
            BankAccount acc2 = new BankAccount("ACC-002", 1000);
            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 100; i++) acc1.deposit(10);
            });
            Thread t2 = new Thread(() -> {
                for (int i = 0; i < 100; i++) acc2.deposit(10);
            });
            Thread t3 = new Thread(() -> {
                for (int i = 0; i < 50; i++) acc1.transferTo(acc2, 5);
            });
            t1.start(); t2.start(); t3.start();
            t1.join(); t2.join(); t3.join();
            System.out.println("  acc1 balance: " + acc1.getBalance());
            System.out.println("  acc2 balance: " + acc2.getBalance());
            System.out.println("  Total: " + (acc1.getBalance() + acc2.getBalance()));
            System.out.println("  Expected total: 4000\n");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [NOT IMPLEMENTED]\n");
        }

        // Test Exercise 2
        System.out.println("--- Exercise 2: Deadlock Prevention with tryLock ---");
        try {
            ResourceAcquisition ra = new ResourceAcquisition();
            Thread t1 = new Thread(ra::work, "Res-A");
            Thread t2 = new Thread(ra::work, "Res-B");
            t1.start(); t2.start();
            t1.join(5000); t2.join(5000);
            System.out.println("  Completed without deadlock.\n");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [NOT IMPLEMENTED]\n");
        }

        // Test Exercise 3
        System.out.println("--- Exercise 3: ReadWriteLock Counter ---");
        try {
            ReadWriteCounter counter = new ReadWriteCounter();
            Runnable reader = () -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("  [" + Thread.currentThread().getName() + "] count = " + counter.readCount());
                    try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            };
            Runnable writer = () -> {
                for (int i = 0; i < 5; i++) {
                    counter.increment();
                    try { Thread.sleep(20); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            };
            Thread r1 = new Thread(reader, "Reader-1");
            Thread r2 = new Thread(reader, "Reader-2");
            Thread w1 = new Thread(writer, "Writer-1");
            r1.start(); r2.start(); w1.start();
            r1.join(); r2.join(); w1.join();
            System.out.println("  Final count: " + counter.readCount() + " (expected 5)\n");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [NOT IMPLEMENTED]\n");
        }

        // Test Exercise 4
        System.out.println("--- Exercise 4: Multi-Resource Lock Ordering ---");
        try {
            MultiResourceLock mrl = new MultiResourceLock();
            Thread t1 = new Thread(mrl::safeWork, "Multi-1");
            Thread t2 = new Thread(mrl::safeWork, "Multi-2");
            t1.start(); t2.start();
            t1.join(5000); t2.join(5000);
            System.out.println("  Completed without deadlock.\n");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [NOT IMPLEMENTED]\n");
        }

        // Test Exercise 5
        System.out.println("--- Exercise 5: Reentrancy Chain ---");
        try {
            ReentrantChain chain = new ReentrantChain();
            Thread t1 = new Thread(chain::methodA, "Reentrant-1");
            Thread t2 = new Thread(chain::methodA, "Reentrant-2");
            t1.start(); t2.start();
            t1.join(); t2.join();
            System.out.println("  Count: " + chain.getCount());
            System.out.println("  Expected: 2222 (each thread: 1+10+100+1000 = 1111, ×2 threads)\n");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [NOT IMPLEMENTED]\n");
        }

        System.out.println("========================================");
        System.out.println("  All exercises complete!");
        System.out.println("========================================");
    }
}
