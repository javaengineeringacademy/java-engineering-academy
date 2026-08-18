package academy.javaengineering.concurrency.threadcreation.solutions;

import java.util.concurrent.*;
import java.util.List;

public class ThreadCreationSolutions {
    public static void main(String[] args) throws Exception {
        // Solution 1: Fibonacci Callable
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Long> fibTask = () -> {
            long a = 0, b = 1;
            for (int i = 0; i < 10; i++) {
                long temp = a + b;
                a = b;
                b = temp;
            }
            return a;
        };
        Future<Long> fibFuture = executor.submit(fibTask);
        System.out.println("Fibonacci(10): " + fibFuture.get());

        // Solution 2: Loop of threads
        for (int i = 0; i < 5; i++) {
            final int index = i;
            new Thread(() -> System.out.println("Thread index: " + index)).start();
        }
        Thread.sleep(100);

        // Solution 3: UncaughtExceptionHandler
        Thread t3 = new Thread(() -> {
            throw new RuntimeException("Test exception");
        });
        t3.setUncaughtExceptionHandler((thread, ex) -> {
            System.out.println("ERROR in " + thread.getName() + ": " + ex.getMessage());
        });
        t3.start();
        t3.join();

        // Solution 4: invokeAny
        List<Callable<String>> tasks = List.of(
            () -> { Thread.sleep(500); return "Slow"; },
            () -> { Thread.sleep(100); return "Fast"; },
            () -> { Thread.sleep(300); return "Medium"; }
        );
        String first = executor.invokeAny(tasks);
        System.out.println("First completed: " + first);

        executor.shutdown();
    }
}
