package academy.javaengineering.concurrency.framework.callable.practices;

import java.util.concurrent.*;

public class Practices {
    public static void main(String[] args) throws Exception {
        System.out.println("Complete the exercises below. Run Solutions.java to check.");
        System.out.println();

        // Exercise 1: Factorial via Callable
        System.out.println("Exercise 1: Write a Callable that computes factorial(10) and submit to ExecutorService");
        System.out.println("Expected: 3628800");
        System.out.println();

        // Exercise 2: Parallel sum of three ranges
        System.out.println("Exercise 2: Submit 3 Callable tasks to sum ranges 1-100, 101-200, 201-300");
        System.out.println("Expected: 45150");
        System.out.println();

        // Exercise 3: get() with timeout
        System.out.println("Exercise 3: Submit a slow task (sleep 3s), use get(1, SECONDS)");
        System.out.println("Expected: TimeoutException, then cancel the task");
        System.out.println();

        // Exercise 4: Cancel running task
        System.out.println("Exercise 4: Submit a long-running Callable, cancel it after 500ms");
        System.out.println("Expected: Cancelled=true, isDone=true");
        System.out.println();

        // Exercise 5: CompletableFuture pipeline
        System.out.println("Exercise 5: Use CompletableFuture to chain: supplyAsync(42) -> multiply by 2 -> toString");
        System.out.println("Expected: 84");
        System.out.println();
    }
}
