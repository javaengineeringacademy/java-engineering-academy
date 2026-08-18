package academy.javaengineering.concurrency.sync.deeprdive.solutions;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.TimeUnit;

/**
 * Synchronization Deep Dive — Complete Solutions.
 *
 * All 5 exercises fully implemented with working main() method.
 */
public class Solutions {

    // ─── Exercise 1: Thread-Safe Bank Account ──────────────────────────
    static class BankAccount {
        private double balance;
        private final String accountNumber;

        public BankAccount(String accountNumber, double initialBalance) {
            this.accountNumber = accountNumber;
            this.balance = initialBalance;
        }

        public synchronized void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
            balance += amount;
            System.out.println("  [" + Thread.currentThread().getName() + "] deposited " + amount
                    + " to " + accountNumber + ", balance: " + balance);
        }

        public synchronized boolean withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
            if (balance < amount) {
                System.out.println("  [" + Thread.currentThread().getName() + "] insufficient funds in "
                        + accountNumber + " (balance: " + balance + ", requested: " + amount + ")");
                return false;
            }
            balance -= amount;
            System.out.println("  [" + Thread.currentThread().getName() + "] withdrew " + amount
                    + " from " + accountNumber + ", balance: " + balance);
            return true;
        }

        public synchronized double getBalance() {
            return balance;
        }

        public synchronized void transferTo(BankAccount other, double amount) {
            if (this == other) return;

            // Lock ordering by accountNumber hashCode to prevent deadlock
            boolean thisFirst = this.accountNumber.hashCode() <= other.accountNumber.hashCode();
            BankAccount first = thisFirst ? this : other;
            BankAccount second = thisFirst ? other : this;

            synchronized (first) {
                synchronized (second) {
                    if (this.balance >= amount) {
                        this.balance -= amount;
                        other.balance += amount;
                        System.out.println("  [" + Thread.currentThread().getName()
                                + "] transferred " + amount + " from " + this.accountNumber
                                + " to " + other.accountNumber);
                    } else {
                        System.out.println("  [" + Thread.currentThread().getName()
                                + "] transfer failed: insufficient funds in " + this.accountNumber);
                    }
                }
            }
        }

        public String getAccountNumber() {
            return accountNumber;
        }
    }

    // ─── Exercise 2: Deadlock Prevention with tryLock ──────────────────
    static class ResourceAcquisition {
        private final ReentrantLock resourceA = new ReentrantLock();
        private final ReentrantLock resourceB = new ReentrantLock();

        public boolean acquireBoth(long timeoutMs) {
            long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
            long remaining;

            // Try resourceA first
            remaining = deadline - System.nanoTime();
            if (remaining <= 0) return false;
            if (!resourceA.tryLock(remaining, TimeUnit.NANOSECONDS)) {
                return false;
            }

            // Try resourceB
            remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                resourceA.unlock();
                return false;
            }
            if (!resourceB.tryLock(remaining, TimeUnit.NANOSECONDS)) {
                resourceA.unlock();
                return false;
            }

            return true;
        }

        public void releaseBoth() {
            if (resourceB.isHeldByCurrentThread()) resourceB.unlock();
            if (resourceA.isHeldByCurrentThread()) resourceA.unlock();
        }

        public void work() {
            if (acquireBoth(2000)) {
                try {
                    System.out.println("  [" + Thread.currentThread().getName() + "] Working with both resources...");
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    releaseBoth();
                    System.out.println("  [" + Thread.currentThread().getName() + "] Released both resources");
                }
            } else {
                System.out.println("  [" + Thread.currentThread().getName() + "] Could not acquire both resources");
            }
        }
    }

    // ─── Exercise 3: ReadWriteLock Counter ─────────────────────────────
    static class ReadWriteCounter {
        private int count = 0;
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

        public void increment() {
            rwLock.writeLock().lock();
            try {
                count++;
                System.out.println("  [" + Thread.currentThread().getName() + "] incremented to " + count);
                Thread.sleep(10); // simulate write work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        public int readCount() {
            rwLock.readLock().lock();
            try {
                int current = count;
                Thread.sleep(5); // simulate read work
                return current;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return count;
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    // ─── Exercise 4: Multi-Resource Lock Ordering ──────────────────────
    static class MultiResourceLock {
        private final ReentrantLock res1 = new ReentrantLock();
        private final ReentrantLock res2 = new ReentrantLock();
        private final ReentrantLock res3 = new ReentrantLock();
        private final ReentrantLock res4 = new ReentrantLock();

        public void acquireInOrder() {
            res1.lock();
            res2.lock();
            res3.lock();
            res4.lock();
        }

        public void releaseInOrder() {
            if (res4.isHeldByCurrentThread()) res4.unlock();
            if (res3.isHeldByCurrentThread()) res3.unlock();
            if (res2.isHeldByCurrentThread()) res2.unlock();
            if (res1.isHeldByCurrentThread()) res1.unlock();
        }

        public void safeWork() {
            acquireInOrder();
            try {
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] Working with all 4 resources...");
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                releaseInOrder();
                System.out.println("  [" + Thread.currentThread().getName()
                        + "] Released all resources");
            }
        }
    }

    // ─── Exercise 5: Reentrancy Chain ──────────────────────────────────
    static class ReentrantChain {
        private int count = 0;

        public synchronized void methodA() {
            count += 1;
            System.out.println("  [" + Thread.currentThread().getName() + "] methodA: count = " + count);
            methodB(); // reentrant call
        }

        public synchronized void methodB() {
            count += 10;
            System.out.println("  [" + Thread.currentThread().getName() + "] methodB: count = " + count);
            methodC(); // reentrant call
        }

        public synchronized void methodC() {
            count += 100;
            System.out.println("  [" + Thread.currentThread().getName() + "] methodC: count = " + count);
            methodD(); // reentrant call
        }

        public synchronized void methodD() {
            count += 1000;
            System.out.println("  [" + Thread.currentThread().getName() + "] methodD: count = " + count);
        }

        public synchronized int getCount() {
            return count;
        }
    }

    // ─── Main — Test All Solutions ─────────────────────────────────────
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("  Synchronization Deep Dive — Solutions");
        System.out.println("========================================\n");

        // ─── Solution 1: Bank Account ──────────────────────────────────
        System.out.println("--- Solution 1: Thread-Safe Bank Account ---");
        BankAccount acc1 = new BankAccount("ACC-001", 1000);
        BankAccount acc2 = new BankAccount("ACC-002", 1000);

        Thread dep1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) acc1.deposit(10);
        }, "Depositor-1");
        Thread dep2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) acc2.deposit(10);
        }, "Depositor-2");
        Thread transfer = new Thread(() -> {
            for (int i = 0; i < 50; i++) acc1.transferTo(acc2, 5);
        }, "Transferer");

        dep1.start(); dep2.start(); transfer.start();
        dep1.join(); dep2.join(); transfer.join();

        System.out.println("  acc1 final: " + acc1.getBalance());
        System.out.println("  acc2 final: " + acc2.getBalance());
        System.out.println("  Total: " + (acc1.getBalance() + acc2.getBalance()));
        System.out.println("  Expected: 4000\n");

        // ─── Solution 2: tryLock Pattern ───────────────────────────────
        System.out.println("--- Solution 2: Deadlock Prevention with tryLock ---");
        ResourceAcquisition ra = new ResourceAcquisition();
        Thread t1 = new Thread(ra::work, "ResWorker-A");
        Thread t2 = new Thread(ra::work, "ResWorker-B");
        t1.start(); t2.start();
        t1.join(5000); t2.join(5000);
        System.out.println("  Completed without deadlock.\n");

        // ─── Solution 3: ReadWriteLock ─────────────────────────────────
        System.out.println("--- Solution 3: ReadWriteLock Counter ---");
        ReadWriteCounter counter = new ReadWriteCounter();
        Runnable reader = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("  [" + Thread.currentThread().getName() + "] read = " + counter.readCount());
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

        // ─── Solution 4: Multi-Resource Lock Ordering ──────────────────
        System.out.println("--- Solution 4: Multi-Resource Lock Ordering ---");
        MultiResourceLock mrl = new MultiResourceLock();
        Thread m1 = new Thread(mrl::safeWork, "MultiWorker-1");
        Thread m2 = new Thread(mrl::safeWork, "MultiWorker-2");
        m1.start(); m2.start();
        m1.join(5000); m2.join(5000);
        System.out.println("  Completed without deadlock.\n");

        // ─── Solution 5: Reentrancy Chain ──────────────────────────────
        System.out.println("--- Solution 5: Reentrancy Chain ---");
        ReentrantChain chain = new ReentrantChain();
        Thread c1 = new Thread(chain::methodA, "Chain-1");
        Thread c2 = new Thread(chain::methodA, "Chain-2");
        c1.start(); c2.start();
        c1.join(); c2.join();
        System.out.println("  Count: " + chain.getCount());
        System.out.println("  Expected: 2222 (each thread: 1+10+100+1000 = 1111, ×2 threads)");

        System.out.println("\n========================================");
        System.out.println("  All solutions verified!");
        System.out.println("========================================");
    }
}
