package academy.javaengineering.concurrency.framework.callable;

import java.util.concurrent.*;

public class CallableFutureExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Callable with Future
        Callable<Long> sumTask = () -> {
            long sum = 0;
            for (int i = 1; i <= 1000000; i++) sum += i;
            return sum;
        };

        Future<Long> future = executor.submit(sumTask);
        System.out.println("Computing sum...");
        System.out.println("Sum: " + future.get());

        // Timeout
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Timeout!");
        }

        // Cancel
        Future<String> cancellable = executor.submit(() -> {
            Thread.sleep(10000);
            return "done";
        });
        Thread.sleep(100);
        boolean cancelled = cancellable.cancel(true);
        System.out.println("Cancelled: " + cancelled);
        System.out.println("Is cancelled: " + cancellable.isCancelled());

        executor.shutdown();
    }
}
