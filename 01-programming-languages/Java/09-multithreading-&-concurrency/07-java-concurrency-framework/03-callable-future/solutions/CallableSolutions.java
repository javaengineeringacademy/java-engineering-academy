package academy.javaengineering.concurrency.framework.callable.solutions;

import java.util.concurrent.*;

public class CallableSolutions {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Solution 1: Factorial
        Callable<Long> factorial = () -> {
            long result = 1;
            for (int i = 2; i <= 10; i++) result *= i;
            return result;
        };
        System.out.println("10! = " + executor.submit(factorial).get());

        // Solution 2: Timeout
        Callable<String> slow = () -> { Thread.sleep(5000); return "done"; };
        Future<String> f = executor.submit(slow);
        try {
            f.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Timed out");
            f.cancel(true);
        }

        // Solution 3: Cancel
        Callable<String> longRunning = () -> {
            for (int i = 0; i < 50; i++) {
                Thread.sleep(200);
            }
            return "completed";
        };
        Future<String> cf = executor.submit(longRunning);
        Thread.sleep(500);
        System.out.println("Cancelled: " + cf.cancel(true));
        System.out.println("isCancelled: " + cf.isCancelled());

        // Solution 4: isDone polling
        Callable<Integer> compute = () -> {
            int sum = 0;
            for (int i = 1; i <= 1_000_000; i++) sum += i;
            return sum;
        };
        Future<Integer> pollFuture = executor.submit(compute);
        while (!pollFuture.isDone()) {
            System.out.print(".");
            Thread.sleep(50);
        }
        System.out.println("\nResult: " + pollFuture.get());

        // Solution 5: ExecutionException
        Callable<String> failing = () -> { throw new RuntimeException("boom"); };
        Future<String> failFuture = executor.submit(failing);
        try {
            failFuture.get();
        } catch (ExecutionException e) {
            System.out.println("Caught: " + e.getCause().getMessage());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
