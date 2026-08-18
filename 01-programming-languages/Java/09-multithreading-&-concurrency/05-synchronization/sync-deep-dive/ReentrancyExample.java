package academy.javaengineering.concurrency.sync.deeprdive;

/**
 * Reentrancy Example.
 *
 * Java intrinsic locks are reentrant: a thread that already holds a lock
 * can re-acquire the same lock without deadlocking itself.
 *
 * The JVM tracks the acquisition count:
 *   - First acquisition: count = 1
 *   - Each re-entry:     count++
 *   - Each exit:         count--
 *   - When count = 0:    lock is released
 *
 * Without reentrancy, the following code would deadlock:
 *   synchronized method A calls synchronized method B (same monitor).
 */
public class ReentrancyExample {

    private int value = 0;
    private final Object lock = new Object();

    // ─── Scenario 1: Reentrant synchronized methods ────────────────────
    public synchronized void outerMethod() {
        System.out.println("  [outerMethod] acquired lock, value = " + value);
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Calling another synchronized method on the same `this` — reentrant!
        innerMethod();

        value++;
        System.out.println("  [outerMethod] done, value = " + value);
    }

    public synchronized void innerMethod() {
        System.out.println("  [innerMethod] re-acquired same lock (reentrant), value = " + value);
        value += 10;
        System.out.println("  [innerMethod] done, value = " + value);
    }

    // ─── Scenario 2: Reentrant synchronized block on same object ───────
    public void blockReentrancy() {
        synchronized (lock) {
            System.out.println("  [block outer] acquired lock, value = " + value);
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Re-entrant: same thread, same lock object
            synchronized (lock) {
                System.out.println("  [block inner] re-acquired same lock, value = " + value);
                value += 100;
                System.out.println("  [block inner] done, value = " + value);
            }

            value += 1;
            System.out.println("  [block outer] done, value = " + value);
        }
    }

    // ─── Scenario 3: Reentrant — synchronized method calls block on `this`
    public synchronized void methodCallsBlock() {
        System.out.println("  [method] acquired lock on this");
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Block on `this` — same lock, reentrant
        synchronized (this) {
            System.out.println("  [block] re-acquired lock on this (reentrant)");
            value += 1000;
        }

        value += 10;
        System.out.println("  [method] done, value = " + value);
    }

    public int getValue() {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrancyExample example = new ReentrancyExample();

        // ─── Test 1: Method calling another synchronized method ─────────
        System.out.println("=== Test 1: Reentrant Synchronized Methods ===");
        Thread t1 = new Thread(() -> {
            example.outerMethod();
        }, "Thread-A");

        t1.start();
        t1.join();
        System.out.println("Final value: " + example.getValue());
        System.out.println("Expected: 11 (innerMethod: +10, outerMethod: +1)");
        System.out.println("No self-deadlock — reentrant!\n");

        // Reset
        ReentrancyExample example2 = new ReentrancyExample();

        // ─── Test 2: Block calling another block on same lock ───────────
        System.out.println("=== Test 2: Reentrant Synchronized Blocks ===");
        Thread t2 = new Thread(() -> {
            example2.blockReentrancy();
        }, "Thread-B");

        t2.start();
        t2.join();
        System.out.println("Final value: " + example2.getValue());
        System.out.println("Expected: 101 (inner: +100, outer: +1)\n");

        // ─── Test 3: Method calls block on same `this` ──────────────────
        ReentrancyExample example3 = new ReentrancyExample();
        System.out.println("=== Test 3: Synchronized Method → Block on `this` ===");
        Thread t3 = new Thread(() -> {
            example3.methodCallsBlock();
        }, "Thread-C");

        t3.start();
        t3.join();
        System.out.println("Final value: " + example3.getValue());
        System.out.println("Expected: 1010 (block: +1000, method: +10)");
    }
}
