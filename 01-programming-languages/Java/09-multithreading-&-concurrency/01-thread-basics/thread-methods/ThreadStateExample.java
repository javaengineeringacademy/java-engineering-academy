package academy.javaengineering.concurrency.basics.methods;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread states and transitions
 */
public class ThreadStateExample {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println("=== Thread States Demo ===\n");

        showAllStates();
        stateTransitions();
    }

    static void showAllStates() {
        System.out.println("--- All 6 Thread States ---");

        // NEW state
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "StateDemo");

        System.out.println("After creation: " + t.getState()); // NEW

        // RUNNABLE state
        t.start();
        System.out.println("After start(): " + t.getState()); // RUNNABLE

        // TIMED_WAITING state (from sleep)
        try {
            Thread.sleep(10); // Let the thread get to sleep
            System.out.println("During sleep: " + t.getState()); // TIMED_WAITING
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Wait for thread to complete
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("After completion: " + t.getState()); // TERMINATED
        System.out.println();
    }

    static void stateTransitions() {
        System.out.println("--- State Transitions ---");

        // BLOCKED state
        Thread blocker1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Blocker1");

        Thread blocker2 = new Thread(() -> {
            synchronized (lock) { // Will block waiting for lock
                System.out.println("Blocker2 acquired lock");
            }
        }, "Blocker2");

        blocker1.start();
        try {
            Thread.sleep(50); // Let blocker1 get lock first
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        blocker2.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Blocker2 state (waiting for lock): " + blocker2.getState()); // BLOCKED

        // Wait for both to finish
        try {
            blocker1.join();
            blocker2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // WAITING state (from join)
        Thread waiter = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Waiter");

        Thread joiner = new Thread(() -> {
            try {
                waiter.join(); // Wait for waiter
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Joiner");

        waiter.start();
        joiner.start();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Joiner state (waiting): " + joiner.getState()); // WAITING or TIMED_WAITING

        // Wait for all to finish
        try {
            waiter.join();
            joiner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Joiner state (after completion): " + joiner.getState()); // TERMINATED
        System.out.println();
    }
}
