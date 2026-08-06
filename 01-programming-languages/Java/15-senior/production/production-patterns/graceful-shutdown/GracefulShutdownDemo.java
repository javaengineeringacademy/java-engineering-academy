package academy.javaengineering.senior.production;

import java.util.concurrent.*;

/**
 * Graceful Shutdown Demo
 * Shutdown hooks, request draining, timeout-based shutdown
 */
public class GracefulShutdownDemo {

    static class GracefulServer {
        private final ExecutorService executor = Executors.newFixedThreadPool(4);
        private volatile boolean shutdownRequested = false;
        private final CountDownLatch shutdownLatch = new CountDownLatch(1);

        public void start() {
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n[Shutdown Hook] Received termination signal");
                initiateShutdown();
            }));

            System.out.println("[Server] Started. Press Ctrl+C to shutdown.");
        }

        public void initiateShutdown() {
            if (shutdownRequested) return;
            shutdownRequested = true;
            System.out.println("[Server] Graceful shutdown initiated");

            // Phase 1: Stop accepting new requests
            System.out.println("[Server] Phase 1: Stopping new request acceptance");

            // Phase 2: Wait for in-progress requests (with timeout)
            System.out.println("[Server] Phase 2: Draining in-progress requests");
            executor.shutdown();

            try {
                boolean drained = executor.awaitTermination(5, TimeUnit.SECONDS);
                if (drained) {
                    System.out.println("[Server] All requests drained successfully");
                } else {
                    System.out.println("[Server] Timeout reached, forcing shutdown of remaining tasks");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                System.out.println("[Server] Shutdown interrupted, forcing");
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }

            // Phase 3: Cleanup resources
            System.out.println("[Server] Phase 3: Cleaning up resources");
            cleanupResources();

            shutdownLatch.countDown();
            System.out.println("[Server] Shutdown complete");
        }

        private void cleanupResources() {
            System.out.println("[Server] Closing database connections");
            System.out.println("[Server] Flushing caches");
            System.out.println("[Server] Releasing file locks");
        }

        public void submitTask(Callable<String> task) {
            if (shutdownRequested) {
                System.out.println("[Server] Rejecting new task - shutdown in progress");
                return;
            }
            executor.submit(() -> {
                System.out.println("[Task] Started: " + Thread.currentThread().getName());
                String result = task.call();
                System.out.println("[Task] Completed: " + result);
                return result;
            });
        }
    }

    static class ConnectionPool {
        private final int maxConnections;
        private int activeConnections = 0;
        private final java.util.concurrent.Semaphore semaphore;

        public ConnectionPool(int maxConnections) {
            this.maxConnections = maxConnections;
            this.semaphore = new java.util.concurrent.Semaphore(maxConnections);
        }

        public boolean acquire(long timeoutMs) throws InterruptedException {
            return semaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS);
        }

        public void release() {
            semaphore.release();
        }

        public void drain() throws InterruptedException {
            System.out.println("[ConnectionPool] Draining " + activeConnections + " active connections");
            // Wait for all connections to be returned
            int waited = 0;
            while (activeConnections > 0 && waited < 5000) {
                Thread.sleep(100);
                waited += 100;
            }
            System.out.println("[ConnectionPool] Drain complete");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Graceful Shutdown Demo ===\n");

        GracefulServer server = new GracefulServer();
        server.start();

        // Submit some long-running tasks
        for (int i = 1; i <= 6; i++) {
            final int taskId = i;
            server.submitTask(() -> {
                Thread.sleep(1000);
                return "Task-" + taskId + " done";
            });
        }

        // Simulate shutdown after 2 seconds
        Thread.sleep(2000);
        System.out.println("\n--- Simulating SIGTERM ---");
        server.initiateShutdown();
        server.shutdownLatch.await(10, TimeUnit.SECONDS);
        System.out.println("\n--- Application terminated ---");
    }
}
