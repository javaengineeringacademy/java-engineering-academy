package academy.javaengineering.concurrency.communication.waitnotify;

/**
 * Compares wait(), sleep(), and join() behavior.
 * Demonstrates lock release, synchronization requirements, and use cases.
 */
public class WaitVsSleepVsJoin {

    private static final Object lock = new Object();

    public static void main(String[] args) {
        System.out.println("=== wait() vs sleep() vs join() ===\n");

        // --- DEMO 1: wait() releases lock ---
        System.out.println("--- Demo 1: wait() RELEASES the lock ---");
        demoWaitReleasesLock();

        // --- DEMO 2: sleep() does NOT release lock ---
        System.out.println("\n--- Demo 2: sleep() does NOT release the lock ---");
        demoSleepDoesNotReleaseLock();

        // --- DEMO 3: join() waits for thread completion ---
        System.out.println("\n--- Demo 3: join() waits for thread completion ---");
        demoJoinWaitsForThread();

        // --- Summary ---
        System.out.println("\n=== Summary ===");
        System.out.println("+-----------+----------------+-------------------+---------------------------+");
        System.out.println("| Method    | Releases Lock? | Requires Sync?    | Purpose                   |");
        System.out.println("+-----------+----------------+-------------------+---------------------------+");
        System.out.println("| wait()    | YES            | YES               | Wait for notification     |");
        System.out.println("| sleep()   | NO             | NO                | Pause for time            |");
        System.out.println("| join()    | NO             | NO                | Wait for thread to finish |");
        System.out.println("+-----------+----------------+-------------------+---------------------------+");
    }

    private static void demoWaitReleasesLock() {
        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[Waiter] Acquired lock, calling wait()...");
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("[Waiter] Re-acquired lock after wait");
            }
        }, "Waiter");

        Thread otherThread = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) {
                System.out.println("[Other] Acquired lock while waiter was waiting (lock was released!)");
                lock.notify();
            }
        }, "OtherThread");

        waiter.start();
        otherThread.start();

        try {
            waiter.join();
            otherThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void demoSleepDoesNotReleaseLock() {
        Thread sleeper = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[Sleeper] Acquired lock, sleeping for 500ms...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("[Sleeper] Still holding lock after sleep");
            }
        }, "Sleeper");

        Thread blockedThread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[Blocked] Trying to acquire lock (will block)...");
            synchronized (lock) {
                System.out.println("[Blocked] Acquired lock after sleeper released it");
            }
        }, "BlockedThread");

        sleeper.start();
        blockedThread.start();

        try {
            sleeper.join();
            blockedThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void demoJoinWaitsForThread() {
        Thread worker = new Thread(() -> {
            try {
                System.out.println("[Worker] Starting work...");
                Thread.sleep(1000);
                System.out.println("[Worker] Work complete");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Worker");

        Thread joiner = new Thread(() -> {
            System.out.println("[Joiner] Waiting for worker to finish...");
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[Joiner] Worker finished, proceeding");
        }, "Joiner");

        worker.start();
        joiner.start();

        try {
            joiner.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
