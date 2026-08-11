package academy.javaengineering.exceptions.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Examples of thread exception handling patterns.
 */
public class ThreadExceptionsExample {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Exception Handling Examples ===\n");

        exampleLoggingUncaughtExceptionHandler();
        exampleThreadGroupHandler();
        exampleExecutorServiceMonitoring();
        exampleCompletableFutureRecovery();
        exampleVirtualThreadException();
    }

    static void exampleLoggingUncaughtExceptionHandler() throws InterruptedException {
        System.out.println("--- Logging UncaughtExceptionHandler ---");

        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            System.err.printf("LOG [%s] Thread: %s, Exception: %s%n",
                Thread.currentThread().getName(), thread.getName(), exception.getMessage());
        });

        Thread thread = new Thread(() -> {
            throw new RuntimeException("Test exception");
        }, "test-thread");

        thread.start();
        thread.join();
        System.out.println();
    }

    static void exampleThreadGroupHandler() throws InterruptedException {
        System.out.println("--- ThreadGroup Handler ---");

        ThreadGroup group = new ThreadGroup("ExceptionGroup") {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println("Group handler: " + t.getName() + " -> " + e.getMessage());
            }
        };

        Thread thread = new Thread(group, () -> {
            throw new RuntimeException("Group exception");
        }, "group-thread");

        thread.start();
        thread.join();
        System.out.println();
    }

    static void exampleExecutorServiceMonitoring() throws Exception {
        System.out.println("--- ExecutorService Exception Monitoring ---");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    if (taskId == 1) {
                        throw new RuntimeException("Task " + taskId + " failed");
                    }
                    System.out.println("Task " + taskId + " completed");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        System.out.println();
    }

    static void exampleCompletableFutureRecovery() {
        System.out.println("--- CompletableFuture Recovery ---");

        CompletableFuture<String> pipeline = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Random failure");
            }
            return "primary-result";
        })
        .exceptionally(ex -> {
            System.out.println("Primary failed: " + ex.getMessage());
            return "fallback-result";
        })
        .thenApply(result -> result.toUpperCase());

        System.out.println("Pipeline result: " + pipeline.join());
        System.out.println();
    }

    static void exampleVirtualThreadException() throws Exception {
        System.out.println("--- Virtual Thread Exception ---");

        CountDownLatch latch = new CountDownLatch(1);

        Thread.startVirtualThread(() -> {
            try {
                throw new RuntimeException("Virtual thread error");
            } catch (Exception e) {
                System.out.println("Virtual thread caught: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
    }
}
