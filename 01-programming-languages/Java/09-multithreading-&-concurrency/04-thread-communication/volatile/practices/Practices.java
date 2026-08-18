package academy.javaengineering.concurrency.communication.volatile.practices;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 5 exercises on volatile keyword.
 * Complete each exercise to master volatile concepts.
 */
public class Practices {

    // TODO 1: Implement a graceful shutdown mechanism
    // Create a class that uses volatile for shutdown signaling.
    // The worker thread should check the flag and stop gracefully.
    static class GracefulShutdown {
        private boolean running = true; // Add volatile here

        public void shutdown() {
            // TODO: Set the flag to stop the worker
        }

        public void startWorker() {
            Thread worker = new Thread(() -> {
                int count = 0;
                while (/* TODO: check condition */) {
                    count++;
                    // Simulate work
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("Worker completed " + count + " iterations");
            });
            worker.start();

            // Let it run for a bit then shutdown
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            shutdown();
        }
    }

    // TODO 2: Fix the race condition using AtomicInteger
    // This counter has a race condition. Fix it using AtomicInteger.
    static class UnsafeCounter {
        private int count = 0; // Fix this!

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    // TODO 3: Implement a thread-safe status flag
    // Create a status checker that uses volatile for status visibility.
    static class StatusChecker {
        // TODO: Add appropriate volatile field for status
        // Status values: 0=IDLE, 1=RUNNING, 2=COMPLETE, 3=ERROR

        public void setStatus(int status) {
            // TODO: Update status
        }

        public int getStatus() {
            // TODO: Return status
            return 0;
        }

        public boolean isRunning() {
            // TODO: Check if status is RUNNING (1)
            return false;
        }
    }

    // TODO 4: Implement safe lazy initialization
    // Fix the broken lazy initialization using volatile.
    static class LazyInit {
        private Object instance; // Add volatile here

        public Object getInstance() {
            if (instance == null) {
                // TODO: Implement safe lazy initialization
                // Use double-checked locking with volatile
            }
            return instance;
        }
    }

    // TODO 5: Detect and fix the visibility problem
    // Thread A sets a flag, Thread B should see it immediately.
    class VisibilityTest {
        private int flag = 0; // Fix this!
        private String message = "";

        public void setFlag(String msg) {
            message = msg;
            flag = 1;
        }

        public String waitForFlag() {
            while (flag == 0) {
                // Busy wait - will this see the flag?
            }
            return message;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Volatile Practice Exercises ===");
        System.out.println();
        System.out.println("Complete the TODO exercises in this file.");
        System.out.println();
        System.out.println("Exercise 1: Implement graceful shutdown using volatile flag");
        System.out.println("Exercise 2: Fix race condition using AtomicInteger");
        System.out.println("Exercise 3: Implement thread-safe status checker");
        System.out.println("Exercise 4: Fix lazy initialization with volatile DCL");
        System.out.println("Exercise 5: Fix visibility problem in flag signaling");
        System.out.println();
        System.out.println("Hints:");
        System.out.println("- volatile ensures visibility, not atomicity");
        System.out.println("- For i++, use AtomicInteger instead");
        System.out.println("- For DCL, use volatile to prevent reordering");
        System.out.println("- Immutable objects + volatile references are thread-safe");
    }
}
