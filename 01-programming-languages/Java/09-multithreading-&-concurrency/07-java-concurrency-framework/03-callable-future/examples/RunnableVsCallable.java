package academy.javaengineering.concurrency.framework.callable;

import java.util.concurrent.*;

public class RunnableVsCallable {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // --- Runnable: no return value ---
        System.out.println("=== Runnable ===");
        Runnable runnableTask = () -> {
            System.out.println("[Runnable] Executing in " + Thread.currentThread().getName());
            // Cannot throw checked exceptions here
            // throw new IOException("checked"); // COMPILE ERROR
        };

        Future<Void> runnableFuture = executor.submit(runnableTask);
        System.out.println("[Runnable] Future result: " + runnableFuture.get()); // always null
        System.out.println("[Runnable] isDone: " + runnableFuture.isDone());

        // --- Callable: returns a value ---
        System.out.println("\n=== Callable ===");
        Callable<Long> callableTask = () -> {
            System.out.println("[Callable] Executing in " + Thread.currentThread().getName());
            long sum = 0;
            for (int i = 1; i <= 1000; i++) {
                sum += i;
            }
            return sum; // returns a value
        };

        Future<Long> callableFuture = executor.submit(callableTask);
        System.out.println("[Callable] Future result: " + callableFuture.get()); // 500500
        System.out.println("[Callable] isDone: " + callableFuture.isDone());

        // --- Exception handling difference ---
        System.out.println("\n=== Exception Handling ===");

        // Runnable exception handling: must catch inside run()
        Runnable failingRunnable = () -> {
            try {
                throw new RuntimeException("Runnable exception");
            } catch (RuntimeException e) {
                System.out.println("[Runnable] Caught inside run(): " + e.getMessage());
            }
        };
        executor.submit(failingRunnable).get(); // no exception thrown to caller

        // Callable exception handling: exception propagates via Future.get()
        Callable<String> failingCallable = () -> {
            throw new RuntimeException("Callable exception");
        };

        Future<String> failingFuture = executor.submit(failingCallable);
        try {
            failingFuture.get();
        } catch (ExecutionException e) {
            System.out.println("[Callable] Caught via Future.get().getCause(): " + e.getCause().getMessage());
        }

        // --- Callable with checked exceptions ---
        System.out.println("\n=== Callable with Checked Exceptions ===");
        Callable<String> checkedCallable = () -> {
            // Callable CAN throw checked exceptions
            throw new java.io.IOException("Checked exception from Callable");
        };

        Future<String> checkedFuture = executor.submit(checkedCallable);
        try {
            checkedFuture.get();
        } catch (ExecutionException e) {
            System.out.println("[Callable] Checked exception via Future: " + e.getCause().getClass().getSimpleName());
        }

        // --- Lambda syntax comparison ---
        System.out.println("\n=== Lambda Syntax ===");
        Runnable r = () -> System.out.println("Runnable lambda");
        Callable<String> c = () -> "Callable lambda result";
        Callable<Integer> c2 = () -> 42;

        r.run();
        System.out.println(c.call());
        System.out.println(c2.call());

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
