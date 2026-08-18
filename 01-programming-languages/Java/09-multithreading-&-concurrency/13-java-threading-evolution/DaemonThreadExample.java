package academy.javaengineering.concurrency.evolution;

import java.util.concurrent.CountDownLatch;

/**
 * Daemon Thread Example
 * 
 * Demonstrates:
 * - What daemon threads are
 * - How they behave differently from user threads
 * - JVM exit behavior with daemon threads
 * - Use cases for daemon threads
 */
public class DaemonThreadExample {

    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== DAEMON THREAD EXAMPLE ===\n");

        demonstrateDaemonThreadBehavior();
        demonstrateJVMExitBehavior();
        demonstrateUseCases();
    }

    /**
     * Shows basic daemon thread behavior
     */
    private static void demonstrateDaemonThreadBehavior() throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         DAEMON THREAD BEHAVIOR                   ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Creating daemon and user threads...\n");

        // User thread (non-daemon)
        Thread userThread = new Thread(() -> {
            System.out.println("  [User Thread] Running (isDaemon: " + 
                Thread.currentThread().isDaemon() + ")");
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("  [User Thread] Working... " + (i + 1));
                    Thread.sleep(200);
                }
                System.out.println("  [User Thread] Finished!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "UserThread");

        // Daemon thread
        Thread daemonThread = new Thread(() -> {
            System.out.println("  [Daemon Thread] Running (isDaemon: " + 
                Thread.currentThread().isDaemon() + ")");
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("  [Daemon Thread] Background work... " + (i + 1));
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "DaemonThread");

        // MUST set daemon BEFORE start()
        daemonThread.setDaemon(true);

        System.out.println("Starting both threads...");
        userThread.start();
        daemonThread.start();

        // Wait for user thread to finish
        userThread.join();

        System.out.println("\n  [Main] User thread finished!");
        System.out.println("  [Main] Daemon thread still running: " + daemonThread.isAlive());
        System.out.println("  [Main] JVM would exit now if no other user threads...");
        System.out.println("  [Main] (In this demo, we continue to show next example)");

        // Give daemon thread a moment to show it's still running
        Thread.sleep(300);
    }

    /**
     * Demonstrates JVM exit behavior with daemon threads
     */
    private static void demonstrateJVMExitBehavior() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         JVM EXIT BEHAVIOR                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Scenario 1: Only daemon threads running");
        System.out.println("  JVM WOULD EXIT immediately (no user threads)\n");

        System.out.println("Scenario 2: User thread + daemon threads");
        System.out.println("  JVM WAITS for user thread to finish\n");

        System.out.println("Scenario 3: Daemon thread calls System.exit()");
        System.out.println("  JVM EXITS immediately (exit permission required)\n");

        // Demonstrate daemon thread trying to keep JVM alive
        System.out.println("--- Demonstrating daemon thread limitation ---\n");

        Thread daemon = new Thread(() -> {
            System.out.println("  [Daemon] Starting infinite loop...");
            int count = 0;
            while (count < 5) {
                System.out.println("  [Daemon] Iteration: " + (++count));
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("  [Daemon] Loop ended (would be killed on JVM exit)");
        });

        daemon.setDaemon(true);
        daemon.start();

        // Main thread is a user thread, so JVM stays alive
        System.out.println("  [Main] Main thread is a USER thread (isDaemon: " + 
            Thread.currentThread().isDaemon() + ")");
        System.out.println("  [Main] JVM stays alive while main thread runs\n");

        daemon.join();
        System.out.println("  [Main] Demonstration complete!");
    }

    /**
     * Shows practical use cases for daemon threads
     */
    private static void demonstrateUseCases() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         DAEMON THREAD USE CASES                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // Use case 1: Background monitoring
        System.out.println("Use Case 1: Background Monitoring Service\n");

        Thread monitor = new Thread(() -> {
            int checkCount = 0;
            while (checkCount < 3) {
                System.out.println("  [Monitor] Checking system health... " + (++checkCount));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "HealthMonitor");

        monitor.setDaemon(true);
        monitor.start();

        System.out.println("  [Main] Starting application...");
        Thread.sleep(500);
        System.out.println("  [Main] Application running...");
        Thread.sleep(500);

        monitor.join();
        System.out.println("  [Main] Monitor finished\n");

        // Use case 2: Timer-based cleanup
        System.out.println("Use Case 2: Cache Cleanup Service\n");

        Thread cacheCleaner = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("  [CacheCleaner] Cleaning expired entries... " + (i + 1));
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("  [CacheCleaner] Cleanup complete");
        }, "CacheCleaner");

        cacheCleaner.setDaemon(true);
        cacheCleaner.start();

        System.out.println("  [Main] Application continues with cache...");
        cacheCleaner.join();
        System.out.println("  [Main] Cache cleaner finished\n");

        // Use case 3: Non-critical background tasks
        System.out.println("Use Case 3: Non-critical Background Tasks\n");

        Thread analytics = new Thread(() -> {
            System.out.println("  [Analytics] Sending event to analytics service...");
            try {
                Thread.sleep(300);
                System.out.println("  [Analytics] Event sent (fire-and-forget)");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "AnalyticsSender");

        analytics.setDaemon(true);
        analytics.start();

        System.out.println("  [Main] Main work continues (analytics is non-critical)");
        analytics.join();

        System.out.println("\n--- Summary ---");
        System.out.println("Daemon threads are ideal for:");
        System.out.println("  • Background monitoring");
        System.out.println("  • Cache cleanup");
        System.out.println("  • Analytics/metrics collection");
        System.out.println("  • Garbage collection");
        System.out.println("  • Any non-critical background task");
        System.out.println("\nRemember: setDaemon(true) MUST be called BEFORE start()!");
    }
}
