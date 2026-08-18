package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates internal mechanics of ExecutorService.
 */
public class ExecutorServiceInternals {

    public static void main(String[] args) throws Exception {
        demonstrateTaskExecution();
        demonstrateWorkerThreads();
        demonstrateFutureTask();
    }

    static void demonstrateTaskExecution() throws Exception {
        System.out.println("=== Task Execution Flow ===");

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        Future<String> future = executor.submit(() -> {
            System.out.println("Executed by: " + Thread.currentThread().getName());
            return "Result from " + Thread.currentThread().getName();
        });

        String result = future.get();
        System.out.println("Retrieved: " + result);

        executor.shutdown();
    }

    static void demonstrateWorkerThreads() throws Exception {
        System.out.println("\n=== Worker Thread Lifecycle ===");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 3, 5L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5)
        );

        System.out.println("Initial pool size: " + executor.getPoolSize());

        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                latch.countDown();
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        latch.await();
        System.out.println("After 3 tasks, pool size: " + executor.getPoolSize());

        executor.shutdown();
    }

    static void demonstrateFutureTask() throws Exception {
        System.out.println("\n=== FutureTask Internals ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> callable = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            return sum;
        };

        Future<Integer> future = executor.submit(callable);
        System.out.println("Future state: " + future); // shows NEW → COMPLETING → NORMAL
        System.out.println("Result: " + future.get());

        executor.shutdown();
    }
}
