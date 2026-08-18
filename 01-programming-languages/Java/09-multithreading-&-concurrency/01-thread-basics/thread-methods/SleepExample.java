package academy.javaengineering.concurrency.basics.methods;

/**
 * sleep() behavior - Pause current thread
 * - sleep(ms) pauses current thread
 * - sleep() does NOT release lock
 * - Interrupt during sleep
 */
public class SleepExample {

    private static final Object lock = new Object();
    private static int counter = 0;

    public static void main(String[] args) {
        System.out.println("=== sleep() Examples ===\n");

        basicSleep();
        sleepDoesNotReleaseLock();
        interruptDuringSleep();
    }

    static void basicSleep() {
        System.out.println("--- Basic sleep(ms) ---");
        System.out.println("[" + System.currentTimeMillis() + "] Thread sleeping 500ms");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("[" + System.currentTimeMillis() + "] Thread woke up");
        System.out.println();
    }

    static void sleepDoesNotReleaseLock() {
        System.out.println("--- sleep() does NOT release lock ---");

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[" + System.currentTimeMillis() + "] T1 acquired lock");
                counter++;
                try {
                    Thread.sleep(1000); // Sleep while holding lock
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("[" + System.currentTimeMillis() + "] T1 releasing lock");
            }
        }, "SleepHolder");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[" + System.currentTimeMillis() + "] T2 acquired lock");
                counter++;
                System.out.println("[" + System.currentTimeMillis() + "] T2 releasing lock");
            }
        }, "LockWaiter");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter: " + counter);
        System.out.println("Note: T2 had to wait for T1 to finish (including sleep)");
        System.out.println();
    }

    static void interruptDuringSleep() {
        System.out.println("--- Interrupt during sleep() ---");

        Thread sleeper = new Thread(() -> {
            System.out.println("[" + System.currentTimeMillis() + "] Thread sleeping 5s");
            try {
                Thread.sleep(5000);
                System.out.println("Sleep completed normally");
            } catch (InterruptedException e) {
                System.out.println("[" + System.currentTimeMillis() + "] Sleep interrupted!");
                System.out.println("Interrupt flag: " + Thread.currentThread().isInterrupted());
            }
        }, "Sleeper");

        sleeper.start();

        try {
            Thread.sleep(1000); // Let it sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("[" + System.currentTimeMillis() + "] Interrupting sleeper thread");
        sleeper.interrupt();

        try {
            sleeper.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
    }
}
