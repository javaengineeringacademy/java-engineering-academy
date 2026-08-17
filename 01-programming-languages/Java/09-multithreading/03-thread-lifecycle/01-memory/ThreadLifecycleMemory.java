package academy.javaengineering.concurrency.lifecycle;

/**
 * ThreadLifecycleMemory - Demonstrates memory aspects of thread lifecycle.
 */
public class ThreadLifecycleMemory {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Monitor Lock Memory ===");
        monitorLockMemory();

        System.out.println("\n=== Wait/Notify Memory ===");
        waitNotifyMemory();

        System.out.println("\n=== Thread Stack During States ===");
        threadStackDuringStates();

        System.out.println("\n=== Lock Release on wait() ===");
        lockReleaseOnWait();
    }

    static void monitorLockMemory() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("  T1: holding lock1, waiting for lock2");
                try { Thread.sleep(100); } catch (InterruptedException e) { return; }
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("  T2: holding lock2, waiting for lock1");
                try { Thread.sleep(100); } catch (InterruptedException e) { return; }
            }
        }, "T2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("  Both threads completed without deadlock");
    }

    static void waitNotifyMemory() throws InterruptedException {
        final Object resource = new Object();
        final boolean[] dataReady = {false};

        Thread producer = new Thread(() -> {
            synchronized (resource) {
                System.out.println("  Producer: preparing data...");
                dataReady[0] = true;
                System.out.println("  Producer: notifying consumer");
                resource.notify();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            synchronized (resource) {
                while (!dataReady[0]) {
                    try {
                        System.out.println("  Consumer: waiting for data...");
                        resource.wait(); // Releases monitor, enters wait set
                        System.out.println("  Consumer: woke up, reacquired monitor");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("  Consumer: processing data");
            }
        }, "Consumer");

        consumer.start();
        Thread.sleep(100);
        producer.start();

        consumer.join();
        producer.join();
    }

    static void threadStackDuringStates() throws InterruptedException {
        Thread t = new Thread(() -> {
            int localVar = 42; // On thread stack
            System.out.println("  RUNNABLE: localVar = " + localVar);
            try {
                Thread.sleep(100); // Stack saved during TIMED_WAITING
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("  Back to RUNNABLE: localVar = " + localVar);
        });

        t.start();
        Thread.sleep(50);
        System.out.println("  Thread state: " + t.getState());
        t.join();
        System.out.println("  Final state: " + t.getState());
    }

    static void lockReleaseOnWait() throws InterruptedException {
        final Object sharedLock = new Object();

        Thread holder = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("  Holder: acquired lock");
                try {
                    System.out.println("  Holder: calling wait() - releasing lock");
                    sharedLock.wait(200);
                    System.out.println("  Holder: reacquired lock after wait");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Holder");

        Thread taker = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { return; }
            synchronized (sharedLock) {
                System.out.println("  Taker: acquired lock (holder released it via wait)");
                System.out.println("  Taker: lock is available because holder called wait()");
            }
        }, "Taker");

        holder.start();
        taker.start();

        holder.join();
        taker.join();
    }
}
