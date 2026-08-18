package academy.javaengineering.concurrency.threadcreation.internals;

import java.util.concurrent.*;

public class CallableInternals {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Callable returns a value via Future
        Callable<String> callable = () -> {
            Thread.sleep(500);
            return "Result from " + Thread.currentThread().getName();
        };

        Future<String> future = executor.submit(callable);
        System.out.println("Future created, isDone: " + future.isDone());
        System.out.println("Waiting for result...");
        String result = future.get(); // blocks
        System.out.println("Result: " + result);
        System.out.println("isDone: " + future.isDone());

        // Exception handling in Callable
        Callable<Integer> failingTask = () -> {
            throw new RuntimeException("Task failed!");
        };
        Future<Integer> failFuture = executor.submit(failingTask);
        try {
            failFuture.get();
        } catch (ExecutionException e) {
            System.out.println("Caught exception: " + e.getCause().getMessage());
        }

        executor.shutdown();
    }
}
