package academy.javaengineering.concurrency.lifecycle;

/**
 * ThreadLifecycleInternals - Demonstrates internal thread state management.
 */
public class ThreadLifecycleInternals {

    private static final Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread State Transitions ===");
        stateTransitions();

        System.out.println("\n=== Monitor Lock Internals ===");
        monitorLockInternals();

        System.out.println("\n=== Wait Set Internals ===");
        waitSetInternals();

        System.out.println("\n=== Sleep Internals ===");
        sleepInternals();
    }

    static void stateTransitions() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                System.out.println("  State while running: " +
                    Thread.currentThread().getState());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("  Before start: " + t.getState());
        t.start();
        System.out.println("  After start (may be RUNNABLE): " + t.getState());
        t.join();
        System.out.println("  After join: " + t.getState());
    }

    static void monitorLockInternals() throws InterruptedException {
        Thread holder = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("  Holder: acquired monitor");
                try { Thread.sleep(500); } catch (InterruptedException e) { return; }
                System.out.println("  Holder: releasing monitor");
            }
        }, "MonitorHolder");

        Thread waiter = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { return; }
            System.out.println("  Waiter: trying to acquire monitor...");
            System.out.println("  Waiter state: " + Thread.currentThread().getState());
            synchronized (monitor) {
                System.out.println("  Waiter: acquired monitor");
            }
        }, "MonitorWaiter");

        holder.start();
        waiter.start();

        holder.join();
        waiter.join();
    }

    static void waitSetInternals() throws InterruptedException {
        Thread waiter = new Thread(() -> {
            synchronized (monitor) {
                try {
                    System.out.println("  Waiter: entering wait set");
                    monitor.wait();
                    System.out.println("  Waiter: removed from wait set");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "WaitSetThread");

        waiter.start();
        Thread.sleep(200);
        System.out.println("  Waiter state while waiting: " + waiter.getState());

        synchronized (monitor) {
            monitor.notify();
        }

        waiter.join();
        System.out.println("  Waiter state after notify: " + waiter.getState());
    }

    static void sleepInternals() throws InterruptedException {
        Thread sleeper = new Thread(() -> {
            try {
                System.out.println("  Sleeper: going to sleep");
                Thread.sleep(500);
                System.out.println("  Sleeper: woke up");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "SleepThread");

        sleeper.start();
        Thread.sleep(100);
        System.out.println("  Sleeper state during sleep: " + sleeper.getState());
        sleeper.join();
        System.out.println("  Sleeper state after completion: " + sleeper.getState());
    }
}
