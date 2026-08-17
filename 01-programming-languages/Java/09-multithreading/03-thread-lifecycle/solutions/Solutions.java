package academy.javaengineering.concurrency.lifecycle;

/**
 * Solutions - Complete solutions for Thread Lifecycle exercises.
 */
public class Solutions {

    private static final Object lock = new Object();

    /**
     * Solution 1: State Transitions
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: State Transitions");
        System.out.println("=============================");

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("  NEW: " + t.getState());

        t.start();
        System.out.println("  RUNNABLE: " + t.getState());

        Thread.sleep(50);
        System.out.println("  TIMED_WAITING: " + t.getState());

        t.join();
        System.out.println("  TERMINATED: " + t.getState());
        System.out.println();
    }

    /**
     * Solution 2: BLOCKED State
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: BLOCKED State");
        System.out.println("=========================");

        final Object sharedLock = new Object();

        Thread holder = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("  Holder: acquired lock, holding for 1s");
                try { Thread.sleep(1000); } catch (InterruptedException e) { return; }
                System.out.println("  Holder: releasing lock");
            }
        }, "LockHolder");

        Thread waiter = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { return; }
            System.out.println("  Waiter state (should be BLOCKED): " +
                Thread.currentThread().getState());
            synchronized (sharedLock) {
                System.out.println("  Waiter: acquired lock");
            }
        }, "LockWaiter");

        holder.start();
        waiter.start();

        holder.join();
        waiter.join();
        System.out.println();
    }

    /**
     * Solution 3: wait/notify Signal
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: wait/notify Signal");
        System.out.println("==============================");

        final Object signalLock = new Object();

        Thread waiter = new Thread(() -> {
            synchronized (signalLock) {
                try {
                    System.out.println("  Waiter: waiting for signal...");
                    signalLock.wait();
                    System.out.println("  Waiter: Signal received!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Waiter");

        Thread notifier = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) { return; }
            synchronized (signalLock) {
                System.out.println("  Notifier: sending signal");
                signalLock.notify();
            }
        }, "Notifier");

        waiter.start();
        notifier.start();

        waiter.join();
        notifier.join();
        System.out.println();
    }

    /**
     * Solution 4: Interrupt During sleep()
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Interrupt During sleep()");
        System.out.println("=====================================");

        Thread sleeper = new Thread(() -> {
            try {
                System.out.println("  Sleeper: going to sleep for 10s");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("  Sleeper: Woke up early!");
            }
        }, "Sleeper");

        sleeper.start();
        Thread.sleep(500);
        System.out.println("  Main: interrupting sleeper");
        sleeper.interrupt();
        sleeper.join();
        System.out.println();
    }

    /**
     * Solution 5: Daemon Thread Lifecycle
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Daemon Thread Lifecycle");
        System.out.println("====================================");

        Thread daemon = new Thread(() -> {
            int count = 0;
            while (true) {
                count++;
                System.out.println("  Daemon heartbeat #" + count);
                try { Thread.sleep(300); } catch (InterruptedException e) { return; }
            }
        }, "DaemonHeartbeat");

        daemon.setDaemon(true);
        daemon.start();

        Thread.sleep(1000);
        System.out.println("  Main done");
        System.out.println("  (Daemon stops when JVM exits)");
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
