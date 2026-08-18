package academy.javaengineering.concurrency.threadcreation.examples;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class ThreadCreationExamples {
    public static void main(String[] args) throws Exception {
        // Example 1: Runnable with lambda
        Thread t1 = new Thread(() -> {
            System.out.println("Lambda Runnable: " + Thread.currentThread().getName());
        }, "LambdaThread");
        t1.start();

        // Example 2: Callable with Future
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Long> sumTask = () -> {
            long sum = 0;
            for (int i = 1; i <= 1000; i++) sum += i;
            return sum;
        };
        Future<Long> future = executor.submit(sumTask);
        System.out.println("Sum of 1-1000: " + future.get());

        // Example 3: Multiple Callables
        List<Callable<String>> tasks = List.of(
            () -> { Thread.sleep(300); return "Task A"; },
            () -> { Thread.sleep(200); return "Task B"; },
            () -> { Thread.sleep(100); return "Task C"; }
        );
        List<Future<String>> futures = executor.invokeAll(tasks);
        for (Future<String> f : futures) {
            System.out.println("Completed: " + f.get());
        }

        // Example 4: Thread with uncaught exception handler
        Thread t4 = new Thread(() -> {
            throw new RuntimeException("Something went wrong!");
        });
        t4.setUncaughtExceptionHandler((thread, ex) -> {
            System.out.println("Uncaught exception in " + thread.getName() + ": " + ex.getMessage());
        });
        t4.start();
        t4.join();

        executor.shutdown();
    }
}
