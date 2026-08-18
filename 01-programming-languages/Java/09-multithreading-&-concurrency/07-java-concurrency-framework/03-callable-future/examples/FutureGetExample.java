package academy.javaengineering.concurrency.framework.callable;

import java.util.concurrent.*;

public class FutureGetExample {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // --- 1. Basic get() blocking ---
        System.out.println("=== 1. Basic get() - Blocking ===");
        Callable<String> slowTask = () -> {
            Thread.sleep(1000);
            return "Result from " + Thread.currentThread().getName();
        };

        Future<String> future1 = executor.submit(slowTask);
        System.out.println("Task submitted, waiting for result...");
        String result = future1.get(); // blocks until result is ready
        System.out.println("Got result: " + result);

        // --- 2. get(timeout) with TimeoutException ---
        System.out.println("\n=== 2. get(timeout) - TimeoutException ===");
        Callable<String> verySlowTask = () -> {
            Thread.sleep(5000); // takes 5 seconds
            return "Slow result";
        };

        Future<String> future2 = executor.submit(verySlowTask);
        try {
            future2.get(1, TimeUnit.SECONDS); // only wait 1 second
        } catch (TimeoutException e) {
            System.out.println("TimeoutException after 1 second!");
            future2.cancel(true); // cancel the slow task
        }

        // --- 3. Handling ExecutionException ---
        System.out.println("\n=== 3. ExecutionException ===");
        Callable<Integer> failingTask = () -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Task failed!");
            }
            return 42;
        };

        Future<Integer> future3 = executor.submit(failingTask);
        try {
            Integer val = future3.get();
            System.out.println("Got value: " + val);
        } catch (ExecutionException e) {
            System.out.println("ExecutionException caught!");
            System.out.println("  Exception type: " + e.getCause().getClass().getSimpleName());
            System.out.println("  Message: " + e.getCause().getMessage());
        }

        // --- 4. InterruptedException from get() ---
        System.out.println("\n=== 4. InterruptedException ===");
        Callable<String> longTask = () -> {
            Thread.sleep(10000);
            return "done";
        };

        Future<String> future4 = executor.submit(longTask);
        Thread.currentThread().interrupt(); // simulate interruption
        try {
            future4.get();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: thread was interrupted while waiting");
        }

        // --- 5. isDone() polling pattern ---
        System.out.println("\n=== 5. isDone() Polling ===");
        Callable<Integer> computeTask = () -> {
            int sum = 0;
            for (int i = 1; i <= 100_000_000; i++) {
                sum += i;
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Task interrupted at iteration " + i);
                    return sum;
                }
            }
            return sum;
        };

        Future<Integer> future5 = executor.submit(computeTask);
        int pollCount = 0;
        while (!future5.isDone()) {
            pollCount++;
            System.out.println("Poll #" + pollCount + " - still computing...");
            Thread.sleep(100);
        }
        System.out.println("Final result: " + future5.get());
        System.out.println("Total polls: " + pollCount);

        // --- 6. get() with cancelled task ---
        System.out.println("\n=== 6. get() on Cancelled Task ===");
        Callable<String> cancelledTask = () -> {
            Thread.sleep(5000);
            return "should not see this";
        };

        Future<String> future6 = executor.submit(cancelledTask);
        Thread.sleep(50); // let it start
        future6.cancel(true);

        try {
            future6.get();
        } catch (CancellationException e) {
            System.out.println("CancellationException: task was cancelled");
        } catch (ExecutionException e) {
            System.out.println("ExecutionException: " + e.getCause());
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
