package multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ThreadCreation - Different ways to create threads
 *
 * Covers:
 * - Extending Thread class
 * - Implementing Runnable
 * - Implementing Callable (with return value)
 * - ExecutorService for thread pooling
 * - Virtual Threads (Java 21+)
 */
public class ThreadCreation {

    public static void main(String[] args) {
        System.out.println("=== Thread Class ===");
        threadClassExample();

        System.out.println("\n=== Runnable Interface ===");
        runnableExample();

        System.out.println("\n=== Callable Interface ===");
        callableExample();

        System.out.println("\n=== ExecutorService ===");
        executorServiceExample();

        System.out.println("\n=== Virtual Threads (Java 21+) ===");
        virtualThreadExample();
    }

    static void threadClassExample() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("Running in: " + Thread.currentThread().getName());
            }
        };
        thread.setName("MyCustomThread");
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void runnableExample() {
        // Anonymous class
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable (anonymous): " + Thread.currentThread().getName());
            }
        };

        // Lambda expression
        Runnable task2 = () -> {
            System.out.println("Runnable (lambda): " + Thread.currentThread().getName());
        };

        // Method reference
        Runnable task3 = ThreadCreation::printCurrentThread;

        new Thread(task1).start();
        new Thread(task2).start();
        new Thread(task3).start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void printCurrentThread() {
        System.out.println("Runnable (method ref): " + Thread.currentThread().getName());
    }

    static void callableExample() {
        // Callable returns a value and can throw exceptions
        Callable<Integer> task = () -> {
            System.out.println("Callable computing in: " + Thread.currentThread().getName());
            Thread.sleep(500);
            return 42;
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(task);

        try {
            Integer result = future.get(); // Blocks until result is available
            System.out.println("Callable result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

    static void executorServiceExample() {
        // Fixed thread pool
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            fixedPool.execute(() -> {
                System.out.println("Fixed pool task " + taskId + ": " +
                    Thread.currentThread().getName());
            });
        }
        fixedPool.shutdown();

        // Single thread executor
        ExecutorService singleThread = Executors.newSingleThreadExecutor();
        singleThread.execute(() -> {
            System.out.println("Single thread: " + Thread.currentThread().getName());
        });
        singleThread.shutdown();

        // Cached thread pool
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            cachedPool.execute(() -> {
                System.out.println("Cached pool task " + taskId + ": " +
                    Thread.currentThread().getName());
            });
        }
        cachedPool.shutdown();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void virtualThreadExample() {
        // Virtual threads (Java 21+) - lightweight threads
        try {
            // Create virtual thread
            Thread virtualThread = Thread.ofVirtual().name("virtual-1").start(() -> {
                System.out.println("Virtual thread: " + Thread.currentThread().getName());
                System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
            });

            virtualThread.join();

            // Virtual thread executor
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 5; i++) {
                    final int taskId = i;
                    executor.submit(() -> {
                        System.out.println("Virtual task " + taskId + ": " +
                            Thread.currentThread().getName());
                    });
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}