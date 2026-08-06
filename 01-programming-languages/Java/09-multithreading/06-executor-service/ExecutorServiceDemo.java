package academy.javaengineering.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceDemo {

    public static void main(String[] args) throws Exception {
        fixedThreadPoolDemo();
        cachedThreadPoolDemo();
        scheduledThreadPoolDemo();
        submitVsExecuteDemo();
        callableFutureDemo();
        invokeAllInvokeAnyDemo();
        shutdownStrategiesDemo();
        rejectedExecutionDemo();
    }

    private static void fixedThreadPoolDemo() {
        System.out.println("=== Fixed Thread Pool ===");
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            fixedPool.execute(() -> {
                System.out.println("Fixed pool - Task " + taskId + " - " + Thread.currentThread().getName());
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        fixedPool.shutdown();
        try { fixedPool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void cachedThreadPoolDemo() {
        System.out.println("\n=== Cached Thread Pool ===");
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            cachedPool.execute(() -> {
                System.out.println("Cached pool - Task " + taskId + " - " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        cachedPool.shutdown();
        try { cachedPool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void scheduledThreadPoolDemo() {
        System.out.println("\n=== Scheduled Thread Pool ===");
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);

        scheduledPool.schedule(() ->
            System.out.println("Scheduled (delay) - " + Thread.currentThread().getName()),
            1, TimeUnit.SECONDS);

        scheduledPool.scheduleAtFixedRate(() ->
            System.out.println("Scheduled (fixed rate) - " + System.currentTimeMillis()),
            0, 500, TimeUnit.MILLISECONDS);

        scheduledPool.scheduleWithFixedDelay(() -> {
            System.out.println("Scheduled (fixed delay) - " + System.currentTimeMillis());
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, 0, 500, TimeUnit.MILLISECONDS);

        scheduledPool.shutdown();
        try { scheduledPool.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void submitVsExecuteDemo() {
        System.out.println("\n=== submit() vs execute() ===");
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.execute(() -> System.out.println("execute() - no return value"));

        Future<String> future = pool.submit(() -> {
            Thread.sleep(100);
            return "submit() result from " + Thread.currentThread().getName();
        });

        try {
            System.out.println("Future.get() = " + future.get(2, TimeUnit.SECONDS));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        pool.shutdown();
    }

    private static void callableFutureDemo() {
        System.out.println("\n=== Callable and Future ===");
        ExecutorService pool = Executors.newFixedThreadPool(3);

        Callable<Integer> callable = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            return sum;
        };

        Future<Integer> future = pool.submit(callable);
        try {
            System.out.println("Sum 1..100 = " + future.get());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int id = i;
            futures.add(pool.submit(() -> "Result-" + id));
        }

        for (Future<String> f : futures) {
            try {
                System.out.println("Callable result: " + f.get());
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
        pool.shutdown();
    }

    private static void invokeAllInvokeAnyDemo() {
        System.out.println("\n=== invokeAll() and invokeAny() ===");
        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int id = i;
            tasks.add(() -> {
                Thread.sleep((long) (Math.random() * 200));
                return "Task-" + id + " done by " + Thread.currentThread().getName();
            });
        }

        try {
            List<Future<String>> allResults = pool.invokeAll(tasks);
            System.out.println("invokeAll() results:");
            for (Future<String> f : allResults) {
                System.out.println("  " + f.get());
            }

            String anyResult = pool.invokeAny(tasks);
            System.out.println("invokeAny() first result: " + anyResult);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        pool.shutdown();
    }

    private static void shutdownStrategiesDemo() {
        System.out.println("\n=== Shutdown Strategies ===");
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 4; i++) {
            final int id = i;
            pool.execute(() -> {
                System.out.println("Running task " + id);
                try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        System.out.println("shutdown() - waits for tasks to complete");
        pool.shutdown();

        try {
            boolean terminated = pool.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("Terminated cleanly: " + terminated);
        } catch (InterruptedException e) {
            System.out.println("Await terminated interrupted");
        }

        ExecutorService pool2 = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 4; i++) {
            final int id = i;
            pool2.execute(() -> {
                System.out.println("Running task " + id + " (shutdownNow)");
                try { Thread.sleep(5000); } catch (InterruptedException e) {
                    System.out.println("Task " + id + " interrupted - cleaning up");
                }
            });
        }

        Thread.sleep(100);
        List<Runnable> notStarted = pool2.shutdownNow();
        System.out.println("shutdownNow() - tasks not started: " + notStarted.size());
    }

    private static void rejectedExecutionDemo() {
        System.out.println("\n=== RejectedExecutionHandler ===");
        RejectedExecutionHandler handler = (runnable, executor) -> {
            System.out.println("Task rejected! Pool: " + executor.toString());
        };

        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2), handler
        );

        for (int i = 0; i < 5; i++) {
            final int id = i;
            try {
                customPool.execute(() -> {
                    System.out.println("Executing task " + id);
                    try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            } catch (RejectedExecutionException e) {
                System.out.println("Task " + id + " rejected with exception");
            }
        }

        customPool.shutdown();
        try { customPool.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("\n=== Thread Pool Sizing Guidelines ===");
        int cpuCores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available CPU cores: " + cpuCores);
        System.out.println("CPU-bound pool size: " + cpuCores + " (cores + 1)");
        System.out.println("IO-bound pool size: " + (cpuCores * 2) + " (cores * 2)");
        System.out.println("Mixed pool size: " + (int) (cpuCores * (1 + 0.1)) + " (cores * (1 + wait/compute ratio))");
    }
}
