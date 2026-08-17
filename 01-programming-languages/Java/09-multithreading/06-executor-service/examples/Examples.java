package academy.javaengineering.concurrency.executor.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ExecutorService examples covering common patterns:
 * - Fixed, cached, and scheduled thread pools
 * - submit() vs execute()
 * - Callable and Future
 * - invokeAll() and invokeAny()
 * - Graceful shutdown
 * - Rejected execution handling
 */
public class Examples {

    public static void main(String[] args) throws Exception {
        fixedThreadPoolExample();
        cachedThreadPoolExample();
        scheduledThreadPoolExample();
        submitVsExecuteExample();
        callableFutureExample();
        invokeAllInvokeAnyExample();
        gracefulShutdownExample();
        rejectedExecutionExample();
    }

    private static void fixedThreadPoolExample() {
        System.out.println("=== Fixed Thread Pool ===\n");
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println("  Task " + taskId + " on " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        pool.shutdown();
        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println();
    }

    private static void cachedThreadPoolExample() {
        System.out.println("=== Cached Thread Pool ===\n");
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println("  Task " + taskId + " on " + Thread.currentThread().getName());
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        pool.shutdown();
        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println();
    }

    private static void scheduledThreadPoolExample() {
        System.out.println("=== Scheduled Thread Pool ===\n");
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        pool.schedule(() ->
            System.out.println("  Delayed task on " + Thread.currentThread().getName()),
            200, TimeUnit.MILLISECONDS);

        pool.scheduleAtFixedRate(() ->
            System.out.println("  Fixed rate: " + System.currentTimeMillis()),
            0, 100, TimeUnit.MILLISECONDS);

        pool.scheduleWithFixedDelay(() -> {
            System.out.println("  Fixed delay: " + System.currentTimeMillis());
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, 0, 100, TimeUnit.MILLISECONDS);

        pool.shutdown();
        try { pool.awaitTermination(2, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println();
    }

    private static void submitVsExecuteExample() {
        System.out.println("=== submit() vs execute() ===\n");
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // execute() — void return, exceptions swallowed
        pool.execute(() -> System.out.println("  execute() — no return value"));

        // submit() — returns Future, exceptions captured
        Future<String> future = pool.submit(() -> {
            Thread.sleep(100);
            return "submit() result from " + Thread.currentThread().getName();
        });

        try {
            System.out.println("  Future.get() = " + future.get(2, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("  Exception: " + e.getMessage());
        }

        pool.shutdown();
        System.out.println();
    }

    private static void callableFutureExample() {
        System.out.println("=== Callable and Future ===\n");
        ExecutorService pool = Executors.newFixedThreadPool(3);

        Callable<Integer> callable = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            return sum;
        };

        Future<Integer> future = pool.submit(callable);
        try {
            System.out.println("  Sum 1..100 = " + future.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("  Exception: " + e.getMessage());
        }

        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int id = i;
            futures.add(pool.submit(() -> "Result-" + id));
        }

        for (Future<String> f : futures) {
            try {
                System.out.println("  " + f.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("  Exception: " + e.getMessage());
            }
        }

        pool.shutdown();
        System.out.println();
    }

    private static void invokeAllInvokeAnyExample() {
        System.out.println("=== invokeAll() and invokeAny() ===\n");
        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int id = i;
            tasks.add(() -> {
                Thread.sleep((long) (Math.random() * 100));
                return "Task-" + id + " done by " + Thread.currentThread().getName();
            });
        }

        try {
            List<Future<String>> allResults = pool.invokeAll(tasks);
            System.out.println("  invokeAll() results:");
            for (Future<String> f : allResults) {
                System.out.println("    " + f.get());
            }

            String anyResult = pool.invokeAny(tasks);
            System.out.println("  invokeAny() first result: " + anyResult);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("  Exception: " + e.getMessage());
        }

        pool.shutdown();
        System.out.println();
    }

    private static void gracefulShutdownExample() {
        System.out.println("=== Graceful Shutdown ===\n");
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 4; i++) {
            final int id = i;
            pool.execute(() -> {
                System.out.println("  Running task " + id);
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        System.out.println("  Calling shutdown()...");
        pool.shutdown();

        try {
            boolean terminated = pool.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("  Terminated cleanly: " + terminated);
        } catch (InterruptedException e) {
            System.out.println("  Await interrupted");
        }
        System.out.println();
    }

    private static void rejectedExecutionExample() {
        System.out.println("=== Rejected Execution ===\n");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        for (int i = 0; i < 6; i++) {
            final int id = i;
            pool.execute(() -> {
                System.out.println("  Executing task " + id + " on " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        pool.shutdown();
        try { pool.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println();
    }
}
