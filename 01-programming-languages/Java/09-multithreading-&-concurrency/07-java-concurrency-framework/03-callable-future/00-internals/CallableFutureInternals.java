package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates internal mechanics of Callable and Future.
 */
public class CallableFutureInternals {

    public static void main(String[] args) throws Exception {
        demonstrateFutureTaskStates();
        demonstrateBlockingGet();
        demonstrateCancellation();
    }

    static void demonstrateFutureTaskStates() throws Exception {
        System.out.println("=== FutureTask States ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Callable<String> slowTask = () -> {
            Thread.sleep(500);
            return "Completed";
        };

        Future<String> future = executor.submit(slowTask);
        System.out.println("Before get: " + future);

        String result = future.get(); // blocks until done
        System.out.println("After get: " + result);

        executor.shutdown();
    }

    static void demonstrateBlockingGet() throws Exception {
        System.out.println("\n=== Blocking get() ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Callable<Integer> longTask = () -> {
            int sum = 0;
            for (int i = 0; i < 1000000; i++) sum += i;
            return sum;
        };

        Future<Integer> future = executor.submit(longTask);

        // Thread parks while waiting
        System.out.println("Waiting for result...");
        Integer result = future.get(); // uses LockSupport.unpark() when complete
        System.out.println("Result: " + result);

        executor.shutdown();
    }

    static void demonstrateCancellation() throws Exception {
        System.out.println("\n=== Cancellation ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Callable<String> slowTask = () -> {
            Thread.sleep(10000);
            return "Done";
        };

        Future<String> future = executor.submit(slowTask);
        Thread.sleep(100);

        boolean cancelled = future.cancel(true); // cancel with interrupt
        System.out.println("Cancelled: " + cancelled);
        System.out.println("Is cancelled: " + future.isCancelled());
        System.out.println("Is done: " + future.isDone());

        try {
            future.get();
        } catch (CancellationException e) {
            System.out.println("get() threw CancellationException");
        }

        executor.shutdown();
    }
}
