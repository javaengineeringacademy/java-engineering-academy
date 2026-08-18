package academy.javaengineering.concurrency.framework.callable.solutions;

import java.util.concurrent.*;

public class Solutions {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // --- Solution 1: Factorial via Callable ---
        System.out.println("=== Solution 1: Factorial ===");
        Callable<Long> factorialTask = () -> {
            long result = 1;
            for (int i = 2; i <= 10; i++) {
                result *= i;
            }
            return result;
        };

        Future<Long> factorialFuture = executor.submit(factorialTask);
        System.out.println("10! = " + factorialFuture.get());
        System.out.println();

        // --- Solution 2: Parallel sum of three ranges ---
        System.out.println("=== Solution 2: Parallel Range Sum ===");
        Callable<Integer> sum1 = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            return sum;
        };
        Callable<Integer> sum2 = () -> {
            int sum = 0;
            for (int i = 101; i <= 200; i++) sum += i;
            return sum;
        };
        Callable<Integer> sum3 = () -> {
            int sum = 0;
            for (int i = 201; i <= 300; i++) sum += i;
            return sum;
        };

        Future<Integer> f1 = executor.submit(sum1);
        Future<Integer> f2 = executor.submit(sum2);
        Future<Integer> f3 = executor.submit(sum3);

        int total = f1.get() + f2.get() + f3.get();
        System.out.println("1-100: " + f1.get());
        System.out.println("101-200: " + f2.get());
        System.out.println("201-300: " + f3.get());
        System.out.println("Total: " + total);
        System.out.println();

        // --- Solution 3: get() with timeout ---
        System.out.println("=== Solution 3: get() with Timeout ===");
        Callable<String> slowTask = () -> {
            Thread.sleep(3000);
            return "completed";
        };

        Future<String> timeoutFuture = executor.submit(slowTask);
        try {
            String result = timeoutFuture.get(1, TimeUnit.SECONDS);
            System.out.println("Got: " + result);
        } catch (TimeoutException e) {
            System.out.println("TimeoutException caught!");
            timeoutFuture.cancel(true);
            System.out.println("Task cancelled: " + timeoutFuture.isCancelled());
        }
        System.out.println();

        // --- Solution 4: Cancel running task ---
        System.out.println("=== Solution 4: Cancel Running Task ===");
        Callable<String> longTask = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("  Running iteration " + i);
                Thread.sleep(100);
            }
            return "done";
        };

        Future<String> cancelFuture = executor.submit(longTask);
        Thread.sleep(500); // let it run for a bit
        boolean cancelled = cancelFuture.cancel(true);
        System.out.println("Cancelled: " + cancelled);
        System.out.println("isCancelled: " + cancelFuture.isCancelled());
        System.out.println("isDone: " + cancelFuture.isDone());

        try {
            cancelFuture.get();
        } catch (CancellationException e) {
            System.out.println("get() threw CancellationException");
        }
        System.out.println();

        // --- Solution 5: CompletableFuture pipeline ---
        System.out.println("=== Solution 5: CompletableFuture Pipeline ===");
        CompletableFuture<String> pipeline = CompletableFuture
            .supplyAsync(() -> 42)
            .thenApply(n -> n * 2)
            .thenApply(Object::toString);
        System.out.println("Result: " + pipeline.get());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
