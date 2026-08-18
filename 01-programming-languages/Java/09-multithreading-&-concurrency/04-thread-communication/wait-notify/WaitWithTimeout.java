package academy.javaengineering.concurrency.communication.waitnotify;

/**
 * Demonstrates wait(timeout) behavior and spurious wakeup handling.
 */
public class WaitWithTimeout {

    private static final Object lock = new Object();
    private static boolean dataArrived = false;

    public static void main(String[] args) {
        System.out.println("=== wait(timeout) Examples ===\n");

        // --- Demo 1: Timeout without notification ---
        System.out.println("--- Demo 1: Timeout expires without notification ---");
        demoTimeoutWithoutNotification();

        // --- Demo 2: Notification before timeout ---
        System.out.println("\n--- Demo 2: Notification arrives before timeout ---");
        demoNotificationBeforeTimeout();

        // --- Demo 3: Spurious wakeup handling ---
        System.out.println("\n--- Demo 3: Handling spurious wakeups ---");
        demoSpuriousWakeup();

        // --- Demo 4: Timeout-based polling ---
        System.out.println("\n--- Demo 4: Timeout-based polling pattern ---");
        demoTimeoutPolling();
    }

    private static void demoTimeoutWithoutNotification() {
        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                long start = System.currentTimeMillis();
                System.out.println("[Waiter] Waiting with 1000ms timeout...");
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("[Waiter] Waited " + elapsed + "ms (timeout expired)");
            }
        }, "Waiter");

        waiter.start();
        try {
            waiter.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void demoNotificationBeforeTimeout() {
        dataArrived = false;

        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                long start = System.currentTimeMillis();
                System.out.println("[Waiter] Waiting with 5000ms timeout...");
                try {
                    lock.wait(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("[Waiter] Woke up after " + elapsed + "ms, dataArrived=" + dataArrived);
            }
        }, "Waiter");

        Thread notifier = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) {
                dataArrived = true;
                lock.notify();
                System.out.println("[Notifier] Notified after 500ms");
            }
        }, "Notifier");

        waiter.start();
        notifier.start();
        try {
            waiter.join();
            notifier.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void demoSpuriousWakeup() {
        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[Waiter] Handling spurious wakeups with while loop");
                int wakeupCount = 0;
                while (!dataArrived) {
                    try {
                        System.out.println("[Waiter] Waiting... (wakeup #" + wakeupCount + ")");
                        lock.wait(500);
                        wakeupCount++;
                        // Check if this was a real notification or timeout/spurious
                        if (!dataArrived) {
                            System.out.println("[Waiter] Spurious wakeup or timeout, re-checking...");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("[Waiter] Data arrived! Proceeding.");
            }
        }, "Waiter");

        Thread notifier = new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) {
                dataArrived = true;
                lock.notify();
                System.out.println("[Notifier] Real notification sent");
            }
        }, "Notifier");

        waiter.start();
        notifier.start();
        try {
            waiter.join();
            notifier.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void demoTimeoutPolling() {
        Thread poller = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[Poller] Polling every 200ms for data...");
                int attempts = 0;
                while (!dataArrived) {
                    try {
                        attempts++;
                        lock.wait(200);
                        if (!dataArrived) {
                            System.out.println("[Poller] Attempt " + attempts + ": no data yet");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("[Poller] Data found after " + attempts + " attempts");
            }
        }, "Poller");

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) {
                dataArrived = true;
                lock.notify();
                System.out.println("[Producer] Data produced");
            }
        }, "Producer");

        poller.start();
        producer.start();
        try {
            poller.join();
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
