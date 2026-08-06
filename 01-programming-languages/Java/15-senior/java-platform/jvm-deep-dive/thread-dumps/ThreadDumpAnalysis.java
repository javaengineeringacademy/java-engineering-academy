package academy.javaengineering.senior.jvm;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread Dump Analysis - Thread states, deadlocks, lock contention.
 *
 * Thread dump commands:
 *   jstack <pid>
 *   jcmd <pid> Thread.print
 *   kill -3 <pid>               (Unix, dumps to stdout)
 *   jvisualvm → Thread Dump button
 *
 * Thread states:
 *   NEW        - Created, not yet started
 *   RUNNABLE   - Executing or ready to execute
 *   BLOCKED    - Waiting for monitor lock
 *   WAITING    - Waiting indefinitely (wait(), join())
 *   TIMED_WAITING - Waiting with timeout (sleep(), wait(ms))
 *   TERMINATED - Completed execution
 */
public class ThreadDumpAnalysis {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    // =====================================================
    // DEMO 1: Thread States
    // =====================================================
    public static void threadStatesDemo() throws InterruptedException {
        System.out.println("=== Thread States ===");

        Thread newThread = new Thread(() -> { /* never started */ });
        System.out.println("NEW:        " + newThread.getState()); // NEW

        Thread runnableThread = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 1000000; i++) sum += i;
        });
        runnableThread.start();
        System.out.println("RUNNABLE:   " + runnableThread.getState()); // RUNNABLE

        Thread waitingThread = new Thread(() -> {
            try {
                new Object().wait(); // waits forever
            } catch (InterruptedException e) { }
        });
        waitingThread.start();
        Thread.sleep(50);
        System.out.println("WAITING:    " + waitingThread.getState()); // WAITING
        waitingThread.interrupt();

        Thread timedThread = new Thread(() -> {
            try {
                Thread.sleep(60000); // timed wait
            } catch (InterruptedException e) { }
        });
        timedThread.start();
        Thread.sleep(50);
        System.out.println("TIMED_WAIT: " + timedThread.getState()); // TIMED_WAITING
        timedThread.interrupt();

        Thread blockedThread = new Thread(() -> {
            synchronized (lockA) {
                // blocked waiting for lockB
                synchronized (lockB) { }
            }
        });
        blockedThread.start();
        Thread.sleep(50);
        System.out.println("States documented above");

        runnableThread.join(1000);
    }

    // =====================================================
    // DEMO 2: Deadlock Detection
    // =====================================================
    public static void deadlockDemo() throws InterruptedException {
        System.out.println("\n=== Deadlock Pattern ===");

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("Thread-1: holding lockA");
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (lockB) {
                    System.out.println("Thread-1: holding lockA + lockB");
                }
            }
        }, "Deadlock-T1");

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("Thread-2: holding lockB");
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (lockA) {
                    System.out.println("Thread-2: holding lockB + lockA");
                }
            }
        }, "Deadlock-T2");

        t1.start();
        t2.start();
        Thread.sleep(500);
        System.out.println("Deadlock created - jstack will show it");
        System.out.println("Look for 'Found one Java-level deadlock' in thread dump");
        t1.interrupt();
        t2.interrupt();
    }

    // =====================================================
    // DEMO 3: Lock Contention Analysis
    // =====================================================
    public static void lockContentionDemo() throws InterruptedException {
        System.out.println("\n=== Lock Contention ===");

        Runnable contendedTask = () -> {
            reentrantLock.lock();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                reentrantLock.unlock();
            }
        };

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(contendedTask, "Contention-" + i);
            threads[i].start();
        }

        for (Thread t : threads) t.join(5000);
        System.out.println("10 threads contended for same lock");
        System.out.println("Thread dump shows threads in BLOCKED state on lock");
    }

    // =====================================================
    // Thread Dump Analysis Checklist
    // =====================================================
    public static void analysisChecklist() {
        System.out.println("\n=== Thread Dump Analysis Checklist ===");
        System.out.println("1. Count thread states");
        System.out.println("   - Many BLOCKED → lock contention");
        System.out.println("   - Many WAITING → thread starvation or deadlock");
        System.out.println("   - Many TIMED_WAITING → excessive sleeping/polling");
        System.out.println();
        System.out.println("2. Identify deadlock");
        System.out.println("   - jstack shows 'Found one Java-level deadlock'");
        System.out.println("   - Look for circular wait: T1 holds A waits B, T2 holds B waits A");
        System.out.println();
        System.out.println("3. Find lock contention");
        System.out.println("   - BLOCKED threads show which lock they wait for");
        System.out.println("   - Check thread name → identify which code path is blocked");
        System.out.println();
        System.out.println("4. Check thread pool saturation");
        System.out.println("   - All threads in pool BLOCKED or WAITING → pool exhausted");
        System.out.println("   - Increase pool size or fix blocking code");
        System.out.println();
        System.out.println("5. Look for thread leaks");
        System.out.println("   - Thread count keeps growing");
        System.out.println("   - Check Thread.getState() == TERMINATED → never joined");
    }

    public static void main(String[] args) throws InterruptedException {
        threadStatesDemo();
        deadlockDemo();
        lockContentionDemo();
        analysisChecklist();
    }
}