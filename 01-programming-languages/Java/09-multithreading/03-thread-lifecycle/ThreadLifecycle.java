package multithreading;

/**
 * ThreadLifecycle - Thread states and transitions
 *
 * Thread States:
 * - NEW: Thread created but not started
 * - RUNNABLE: Executing or ready to execute
 * - BLOCKED: Waiting to acquire monitor lock
 * - WAITING: Waiting indefinitely for another thread
 * - TIMED_WAITING: Waiting for specified time
 * - TERMINATED: Completed execution
 */
public class ThreadLifecycle {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread States ===");
        threadStates();

        System.out.println("\n=== NEW State ===");
        newStateDemo();

        System.out.println("\n=== RUNNABLE State ===");
        runnableStateDemo();

        System.out.println("\n=== BLOCKED State ===");
        blockedStateDemo();

        System.out.println("\n=== WAITING State ===");
        waitingStateDemo();

        System.out.println("\n=== TIMED_WAITING State ===");
        timedWaitingDemo();

        System.out.println("\n=== TERMINATED State ===");
        terminatedStateDemo();
    }

    static void threadStates() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("After creation: " + thread.getState()); // NEW

        thread.start();
        System.out.println("After start: " + thread.getState()); // RUNNABLE

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("After completion: " + thread.getState()); // TERMINATED
    }

    static void newStateDemo() {
        Thread thread = new Thread(() -> {
            System.out.println("Thread running");
        });

        System.out.println("State before start: " + thread.getState());
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void runnableStateDemo() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                Math.sqrt(i);
            }
        });

        thread.start();

        // Check state during execution
        System.out.println("During execution: " + thread.getState());

        thread.join();
        System.out.println("After execution: " + thread.getState());
    }

    static void blockedStateDemo() throws InterruptedException {
        Thread blocked1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Blocked1");

        Thread blocked2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("Blocked2 acquired lock");
            }
        }, "Blocked2");

        blocked1.start();
        Thread.sleep(100); // Ensure blocked1 holds the lock
        blocked2.start();

        System.out.println("Blocked2 state: " + blocked2.getState()); // BLOCKED

        blocked1.join();
        blocked2.join();
    }

    static void waitingStateDemo() throws InterruptedException {
        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait(); // Release lock and wait
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "WaitingThread");

        waitingThread.start();
        Thread.sleep(100);

        System.out.println("Waiting thread state: " + waitingThread.getState()); // WAITING

        synchronized (lock) {
            lock.notify(); // Wake up waiting thread
        }

        waitingThread.join();
        System.out.println("After notify: " + waitingThread.getState());
    }

    static void timedWaitingDemo() throws InterruptedException {
        Thread timedThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // TIMED_WAITING
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "TimedThread");

        timedThread.start();
        Thread.sleep(100);

        System.out.println("Timed thread state: " + timedThread.getState()); // TIMED_WAITING

        timedThread.join();
        System.out.println("After completion: " + timedThread.getState());
    }

    static void terminatedStateDemo() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("Thread executing");
        });

        thread.start();
        thread.join();

        System.out.println("Terminated state: " + thread.getState());
        System.out.println("Is alive: " + thread.isAlive()); // false
    }
}