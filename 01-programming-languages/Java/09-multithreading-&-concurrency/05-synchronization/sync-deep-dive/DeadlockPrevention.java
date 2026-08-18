package academy.javaengineering.concurrency.sync.deeprdive;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

/**
 * Deadlock Prevention Techniques.
 *
 * Three approaches:
 *   1. Lock Ordering — always acquire locks in the same global order
 *   2. tryLock with Timeout — don't wait forever, back off if lock unavailable
 *   3. Lock Hierarchy — define a strict hierarchy, always lock parent before child
 */
public class DeadlockPrevention {

    // ─── Approach 1: Lock Ordering ─────────────────────────────────────
    static class LockOrdering {
        private static final Object lockA = new Object();
        private static final Object lockB = new Object();

        /**
         * Both threads acquire lockA first, then lockB.
         * Consistent order prevents deadlock.
         */
        public static void threadA() {
            synchronized (lockA) {
                System.out.println("[Ordering-A] acquired lockA");
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (lockB) {
                    System.out.println("[Ordering-A] acquired lockB — both locks held");
                }
                System.out.println("[Ordering-A] released both locks");
            }
        }

        public static void threadB() {
            synchronized (lockA) {  // SAME ORDER as threadA
                System.out.println("[Ordering-B] acquired lockA");
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (lockB) {
                    System.out.println("[Ordering-B] acquired lockB — both locks held");
                }
                System.out.println("[Ordering-B] released both locks");
            }
        }
    }

    // ─── Approach 2: tryLock with Timeout ──────────────────────────────
    static class TryLockPattern {
        private static final ReentrantLock lockA = new ReentrantLock();
        private static final ReentrantLock lockB = new ReentrantLock();

        public static void threadA() {
            boolean acquiredA = false;
            boolean acquiredB = false;
            try {
                acquiredA = lockA.tryLock(2, TimeUnit.SECONDS);
                if (!acquiredA) {
                    System.out.println("[TryLock-A] could not acquire lockA, backing off");
                    return;
                }
                System.out.println("[TryLock-A] acquired lockA");
                Thread.sleep(50);

                acquiredB = lockB.tryLock(2, TimeUnit.SECONDS);
                if (!acquiredB) {
                    System.out.println("[TryLock-A] could not acquire lockB, backing off");
                    return;
                }
                System.out.println("[TryLock-A] acquired lockB — both locks held");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (acquiredB) lockB.unlock();
                if (acquiredA) lockA.unlock();
                System.out.println("[TryLock-A] released all locks");
            }
        }

        public static void threadB() {
            boolean acquiredA = false;
            boolean acquiredB = false;
            try {
                acquiredB = lockB.tryLock(2, TimeUnit.SECONDS);
                if (!acquiredB) {
                    System.out.println("[TryLock-B] could not acquire lockB, backing off");
                    return;
                }
                System.out.println("[TryLock-B] acquired lockB");
                Thread.sleep(50);

                acquiredA = lockA.tryLock(2, TimeUnit.SECONDS);
                if (!acquiredA) {
                    System.out.println("[TryLock-B] could not acquire lockA, backing off");
                    return;
                }
                System.out.println("[TryLock-B] acquired lockA — both locks held");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (acquiredA) lockA.unlock();
                if (acquiredB) lockB.unlock();
                System.out.println("[TryLock-B] released all locks");
            }
        }
    }

    // ─── Approach 3: Lock Hierarchy ────────────────────────────────────
    static class LockHierarchy {
        // Hierarchy: database > table > row (always lock parent first)
        private static final ReentrantLock dbLock = new ReentrantLock();
        private static final ReentrantLock tableLock = new ReentrantLock();
        private static final ReentrantLock rowLock = new ReentrantLock();

        public static void safeOperation() {
            // Always lock in hierarchy order: db > table > row
            dbLock.lock();
            try {
                System.out.println("[Hierarchy] acquired dbLock");
                tableLock.lock();
                try {
                    System.out.println("[Hierarchy] acquired tableLock");
                    rowLock.lock();
                    try {
                        System.out.println("[Hierarchy] acquired rowLock — all 3 levels held");
                        Thread.sleep(10);
                    } finally {
                        rowLock.unlock();
                        System.out.println("[Hierarchy] released rowLock");
                    }
                } finally {
                    tableLock.unlock();
                    System.out.println("[Hierarchy] released tableLock");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                dbLock.unlock();
                System.out.println("[Hierarchy] released dbLock");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // ─── Demo 1: Lock Ordering ─────────────────────────────────────
        System.out.println("=== Approach 1: Lock Ordering ===");
        System.out.println("Both threads acquire lockA first, then lockB.");
        System.out.println("Consistent order → no deadlock.\n");

        Thread t1 = new Thread(LockOrdering::threadA, "Ordering-A");
        Thread t2 = new Thread(LockOrdering::threadB, "Ordering-B");
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Completed without deadlock.\n");

        // ─── Demo 2: tryLock with Timeout ──────────────────────────────
        System.out.println("=== Approach 2: tryLock with Timeout ===");
        System.out.println("Threads try to acquire locks with 2-second timeout.");
        System.out.println("If timeout expires, they back off → no deadlock.\n");

        Thread t3 = new Thread(TryLockPattern::threadA, "TryLock-A");
        Thread t4 = new Thread(TryLockPattern::threadB, "TryLock-B");
        t3.start(); t4.start();
        t3.join(); t4.join();
        System.out.println("Completed without deadlock.\n");

        // ─── Demo 3: Lock Hierarchy ────────────────────────────────────
        System.out.println("=== Approach 3: Lock Hierarchy ===");
        System.out.println("Locks acquired in strict order: db > table > row.\n");

        Thread t5 = new Thread(LockHierarchy::safeOperation, "Hierarchy-1");
        Thread t6 = new Thread(LockHierarchy::safeOperation, "Hierarchy-2");
        t5.start(); t6.start();
        t5.join(); t6.join();
        System.out.println("Completed without deadlock.");

        System.out.println("\n=== Summary ===");
        System.out.println("Lock Ordering:  Assign numeric order to locks, always acquire in ascending order.");
        System.out.println("tryLock:        Don't wait forever — use timeout and back off on failure.");
        System.out.println("Hierarchy:      Define strict nesting order, never acquire a lower-level lock first.");
    }
}
